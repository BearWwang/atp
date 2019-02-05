package libs

import (
	"bytes"
	"context"
	"errors"
	"fmt"
	"github.com/astaxie/beego/logs"
	"github.com/gpmgo/gopm/modules/log"
	"io/ioutil"
	"math"
	"net/http"
	"strings"
	"sync/atomic"
	"time"
)

const (
	STATUS_ORIGINAL uint32 = 0
	STATUS_STARTING uint32 = 1
	STATUS_STARTED  uint32 = 2
	STATUS_STOPING  uint32 = 3
	STATUS_STOPED   uint32 = 4
)

const (
	RET_CODE_SUCCESS              RetCode = 0    // 成功。
	RET_CODE_WARNING_CALL_TIMEOUT         = 1001 // 调用超时警告。
	RET_CODE_ERROR_CALL                   = 2001 // 调用错误。
	RET_CODE_ERROR_RESPONSE               = 2002 // 响应内容错误。
	RET_CODE_ERROR_CALEE                  = 2003 // 被调用方（被测软件）的内部错误。
	RET_CODE_FATAL_CALL                   = 3001 // 调用过程中发生了致命错误！
)

func GetRetCodePlain(code RetCode) string {
	var codePlain string
	switch code {
	case RET_CODE_SUCCESS:
		codePlain = "Success"
	case RET_CODE_WARNING_CALL_TIMEOUT:
		codePlain = "Call Timeout Warning"
	case RET_CODE_ERROR_CALL:
		codePlain = "Call Error"
	case RET_CODE_ERROR_RESPONSE:
		codePlain = "Response Error"
	case RET_CODE_ERROR_CALEE:
		codePlain = "Callee Error"
	case RET_CODE_FATAL_CALL:
		codePlain = "Call Fatal Error"
	default:
		codePlain = "Unknown result code"
	}
	return codePlain
}

type RetCode int

type Caller interface {
	BuildRequest() RawReq
	Call(req []byte, timeoutNs time.Duration) ([]byte, error)
	CheckResponse(req RawReq, resp RawResp) *CallResult
}
type GoTickets interface {
	Take()
	Return()
	Active() bool
	Total() uint32
	Remainder() uint32
}

type Generator interface {
	Start() bool       // 启动载荷发生器
	Stop() bool        //停止载荷发生器Á¸
	Status() uint32    //获取状态
	CallCount() uint32 //调用计数
}

type CallResult struct {
	Id         int64
	Request    RawReq
	Response   RawResp
	StatusCode RetCode
	Msg        string
	Elapse     time.Duration
}

type RawReq struct {
	Id  int64
	Req []byte
}

type RawResp struct {
	Id     int64
	Resp   []byte
	Err    error
	Elapse time.Duration
}

/**********************************************************************************************************************/

/************Generator*******************/
type TGenerator struct {
	caller      Caller             // 调用器
	timeout     time.Duration      // 超时时间 NS
	lps         uint32             // 每秒的载荷量
	duration    time.Duration      // 负载持续时间
	concurrency uint32             // 载荷并发量
	tickets     GoTickets          // goroutine池
	ctx         context.Context    // 上下文
	cancelFunc  context.CancelFunc // 调用统计
	status      uint32             // 状态
	resultCh    chan *CallResult   // 调用结果通道
	callCount   uint32
}

func (gen *TGenerator) Start() bool {
	// 检查是否具备可启动的状态，顺便设置状态为正在启动
	logs.Info("start....")
	if !atomic.CompareAndSwapUint32(&gen.status, STATUS_ORIGINAL, STATUS_STARTING) {
		if !atomic.CompareAndSwapUint32(&gen.status, STATUS_STOPED, STATUS_STARTING) {
			return false
		}
	}

	logs.Info("start succeed")

	var throttle <-chan time.Time
	if gen.lps > 0 {
		interval := time.Duration(1e9 / gen.lps)
		throttle = time.Tick(interval)
	}
	logs.Info("running ...")

	// 在运行一段时间后可以停下来
	gen.ctx, gen.cancelFunc = context.WithTimeout(context.Background(), gen.duration)
	gen.callCount = 0

	atomic.StoreUint32(&gen.status, STATUS_STARTED)

	go func() {
		gen.genLoad(throttle)
	}()
	return true
}

func (gen *TGenerator) genLoad(throttle <-chan time.Time) {
	for {
		select {
		case <-gen.ctx.Done():
			logs.Info("Done..")
			gen.prepareToStop(gen.ctx.Err())
			return
		default:
			gen.prepareToStop(gen.ctx.Err())
		}
		//
		if gen.lps > 0 {
			select {
			case <-throttle:
			case <-gen.ctx.Done():
				logs.Info("Done..")
				gen.prepareToStop(gen.ctx.Err())
				return
			}
		}
	}
}

func (gen *TGenerator) prepareToStop(ctxErr error) {
	log.Info("prepare to stop load generator cause(%s)", ctxErr)
	atomic.CompareAndSwapUint32(&gen.status, STATUS_STARTED, STATUS_STOPING)
	log.Info("Closing result channel..")
	close(gen.resultCh)
	atomic.StoreUint32(&gen.status, STATUS_STOPED)

}

func (gen *TGenerator) Stop() bool {
	if !atomic.CompareAndSwapUint32(&gen.status, STATUS_STARTED, STATUS_STOPING) {
		return false
	}
	gen.cancelFunc()

	for {
		if atomic.LoadUint32(&gen.status) == STATUS_STARTED {
			break;
		}
		time.Sleep(time.Microsecond)
	}
	return true
}

func (gen *TGenerator) Status() uint32 {
	return atomic.LoadUint32(&gen.status)
}

func (gen *TGenerator) CallCount() uint32 {
	return atomic.LoadUint32(&gen.callCount)
}

func NewGenerator(param Param) (*TGenerator, error) {
	log.Info("new load generator")
	var err error

	if err = param.Check(); err != nil {
		return nil, err
	}
	gen := &TGenerator{
		caller:   param.Caller,
		timeout:  param.Timeout,
		lps:      param.Lps,
		duration: param.Duration,
		status:   STATUS_ORIGINAL,
		resultCh: param.ResCh,
	}

	if err := gen.init(); err != nil {
		return nil, err
	}

	return gen, nil
}

func (gen *TGenerator) init() error {
	var buf bytes.Buffer
	buf.WriteString("initializing the load generator")
	// 载荷的并发量 ≈ 载荷的响应超时时间 / 载荷的发送间隔时间
	var total64 = int64(gen.timeout)/int64(1e9/gen.lps) + 1
	if total64 > math.MaxInt32 {
		total64 = math.MaxInt32
	}
	gen.concurrency = uint32(total64)

	ticket, err := NewTickets(gen.concurrency)
	if err != nil {
		return err
	}

	gen.tickets = ticket

	buf.WriteString(fmt.Sprintf("Done,concurrency is %d", gen.concurrency))
	return nil

}

func (gen *TGenerator) CallOne(req *RawReq) (res *RawResp) {
	atomic.AddUint32(&gen.callCount, 1)
	if req == nil {
		res = &RawResp{Id: -1, Err: errors.New("Invalid request")}
		return
	}

	start := time.Now().UnixNano()
	resp, err := gen.caller.Call(req.Req, gen.timeout)
	end := time.Now().UnixNano()

	cost := time.Duration(end - start)

	if err != nil {
		res = &RawResp{
			Id:     req.Id,
			Err:    errors.New(fmt.Sprintf("Sync Call Error: %s.", err)),
			Elapse: cost,
		}
	} else {
		res = &RawResp{
			Id:     req.Id,
			Resp:   resp,
			Elapse: cost,
		}
	}
	return
}

func (gen *TGenerator) asyncCall() {
	gen.tickets.Take()

	go func() {

		defer func() {
			if p := recover(); p != nil {
				err, ok := interface{}(p).(error)
				var errMsg string
				if ok {
					errMsg = fmt.Sprintf("Async Call Panic! (error: %s)", err)
				} else {
					errMsg = fmt.Sprintf("Async Call Panic! (error: %#v)", p)
				}
				logs.Info(errMsg)

				result := &CallResult{
					Id:         -1,
					StatusCode: RET_CODE_ERROR_CALL,
					Msg:        errMsg,
				}
				gen.sendResult(result)
			}
			gen.tickets.Return()
		}()

		req := gen.caller.BuildRequest()

		// 调用状态：0-未调用或调用中；1-调用完成；2-调用超时。
		var status uint32
		timer := time.AfterFunc(gen.timeout, func() {
			if atomic.CompareAndSwapUint32(&status, 0, 2) {
				return
			}
			result := &CallResult{
				Id:         req.Id,
				Request:    req,
				StatusCode: RET_CODE_WARNING_CALL_TIMEOUT,
				Msg:        fmt.Sprintf("Timeout! (expected: < %v)", gen.timeout),
				Elapse:     gen.timeout,
			}
			gen.sendResult(result)
		})

		resp := gen.CallOne(&req)

		if atomic.CompareAndSwapUint32(&status, 0, 1) {
			return
		}
		timer.Stop()

		var res *CallResult

		if resp.Err != nil {
			res = &CallResult{
				Id:         req.Id,
				Request:    req,
				StatusCode: RET_CODE_ERROR_CALEE,
				Msg:        resp.Err.Error(),
				Elapse:     resp.Elapse,
			}
		} else {
			res = gen.caller.CheckResponse(req, *resp)
			res.Elapse = resp.Elapse
		}
		gen.sendResult(res)

	}()
}

func (gen *TGenerator) sendResult(result *CallResult) bool {
	if atomic.LoadUint32(&gen.status) != STATUS_STARTED {
		gen.printIgnoredResult(result, "stopped load generator")
		return false
	}
	select {
	case gen.resultCh <- result:
		return true
	default:
		gen.printIgnoredResult(result, "full result channel")
		return false
	}
}

func (gen *TGenerator) printIgnoredResult(result *CallResult, cause string) {
	resultMsg := fmt.Sprintf(
		"ID=%d, Code=%d, Msg=%s, Elapse=%v",
		result.Id, result.StatusCode, result.Msg, result.Elapse)
	logs.Error("Ignored result: %s. (cause: %s)\n", resultMsg, cause)
}

/************Param*******************/

type Param struct {
	Caller   Caller
	Timeout  time.Duration
	Lps      uint32
	Duration time.Duration
	ResCh    chan *CallResult
}

func (p *Param) Check() error {
	errMsg := make([]string, 0)
	if p.Caller == nil {
		errMsg = append(errMsg, "Invalid caller!")
	}

	if p.Timeout == 0 {
		errMsg = append(errMsg, "Invalid timeout!")
	}

	if p.Lps == 0 {
		errMsg = append(errMsg, "Invalid lps!")
	}

	if p.Duration == 0 {
		errMsg = append(errMsg, "Invalid duration!")
	}
	if p.ResCh == nil {
		errMsg = append(errMsg, "Invalid result channel!")
	}

	var buf bytes.Buffer

	buf.WriteString("Checking this params...")

	if len(errMsg) != 0 {
		er := strings.Join(errMsg, ", ")
		buf.WriteString(fmt.Sprintf("No Passed because (%s)", er))
		return errors.New(buf.String())
	}

	buf.WriteString(fmt.Sprintf("Passed,timeout=%s,lps=%d,Duration=%s", p.Timeout, p.Lps, p.Duration))
	log.Info(buf.String())
	return nil
}

/******************************Generator**************************************/
type TGoTicket struct {
	total    uint32        // 总数
	ticketCh chan struct{} // 容器
	active   bool          //是否被激活
}

func (ticket *TGoTicket) Take() {
	<-ticket.ticketCh
}

func (ticket *TGoTicket) Return() {
	ticket.ticketCh <- struct{}{}
}

func (ticket *TGoTicket) Active() bool {
	return ticket.active
}

func (ticket *TGoTicket) Total() uint32 {
	return ticket.total
}

func (ticket *TGoTicket) Remainder() uint32 {
	return uint32(len(ticket.ticketCh))
}

func (ticket *TGoTicket) init(total uint32) bool {
	if ticket.active {
		return false;
	}
	if total == 0 {
		return false;
	}
	ch := make(chan struct{}, total)
	n := int(total)
	for i := 0; i < n; i++ {
		ch <- struct{}{}
	}

	ticket.ticketCh = ch
	ticket.total = total
	ticket.active = true;
	return true;
}

func NewTickets(total uint32) (*TGoTicket, error) {
	ticket := TGoTicket{}
	if !ticket.init(total) {
		errMsg := fmt.Sprintf("The goroutine ticket pool can not be initalized (total=%d)\n", total)
		return nil, errors.New(errMsg)
	}

	return &ticket, nil
}

/********************Request*****************************/

type Connection struct {
	Url string
}

func NewConnection(url string) Caller {
	return &Connection{Url: url}
}

func (c *Connection) BuildRequest() RawReq {

	id := time.Now().UnixNano()
	req := RawReq{
		Id:  id,
		Req: []byte(c.Url),
	}
	return req
}

func (c *Connection) Call(req []byte, timeoutNs time.Duration) ([]byte, error) {
	client := http.DefaultClient
	client.Timeout = timeoutNs
	res, err := client.Get(string(req))
	if err != nil {
		return nil, err
	}

	byt, err := ioutil.ReadAll(res.Body)
	if err != nil {
		return nil, err
	}
	return byt, nil
}

func (*Connection) CheckResponse(req RawReq, resp RawResp) *CallResult {
	var result CallResult
	result.Id = req.Id
	result.Request = req
	result.Response = resp
	if len(resp.Resp) == 0 {
		result.StatusCode = RET_CODE_FATAL_CALL
		result.Msg = fmt.Sprintf("Nothing...")
		return &result
	}
	result.StatusCode = RET_CODE_SUCCESS
	result.Msg = fmt.Sprintf("Success")
	return &result
}

func main() {
	P := Param{
		Caller:   NewConnection("http://www.baidu.com"),
		Timeout:  time.Second * 10,
		Lps:      20,
		Duration: time.Second * 5,
		ResCh:    make(chan *CallResult),
	}
	gen, err := NewGenerator(P)
	if err != nil {
		panic(err)
	}

	if gen.Start() {
		gen.Stop()
	}
}
