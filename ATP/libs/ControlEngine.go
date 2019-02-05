package libs

import (
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"os"
	"reflect"
	"sync"
	"time"
)

var Engine *ControlEngine

func init() {
	Engine = NewEngine()
}

type ControlEngine struct {
}

type Params struct {
	param []reflect.Value
}

func (p *Params) AddParam(par interface{}) {
	p.param = append(p.param, reflect.ValueOf(par))
}

func (p *Params) Len() int {
	return len(p.param)
}

func (p *Params) GetParams() []reflect.Value {
	return p.param
}

func (engine *ControlEngine) Update(msg Message) {
	val := reflect.ValueOf(msg.Msg)
	switch val.Kind() {
	case reflect.Func:
		values := val.Call(msg.Params.GetParams())
		for _, v := range values {
			beego.Info(v.Kind().String())
		}
	case reflect.String:
		logs.Info(val.String())
	case reflect.Ptr:
		errorLog := fmt.Sprintf("%+v", val)
		times := time.Now().Format("2006-01-02")
		beego.Info(errorLog)
		f, err := os.OpenFile(beego.AppPath+"/files/log/error/"+times+".log", os.O_CREATE|os.O_RDWR|os.O_APPEND, 0666)
		if err != nil {
			beego.Error(err)
		}
		f.WriteString(errorLog)
		defer f.Close()
	default:
	}
}

func NewEngine() *ControlEngine {
	var instance *ControlEngine
	var once sync.Once
	once.Do(func() {
		instance = new(ControlEngine)
	})
	return instance
}
