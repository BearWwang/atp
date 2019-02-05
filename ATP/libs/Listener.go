package libs

import (
	"github.com/joomcode/errorx"
)

//var lock = sync.Mutex{}

// 监听者接口
type Listener interface {
	Update(msg Message)
}

// 被监听者
type Listenable struct {
	queue   Listener
	changed bool
}

// 传递的消息
type Message struct {
	Msg    interface{}
	Params Params
	FileName string
}

func NewErrorMsg(err error, funcName string) Message {
	msg := new(Message)
	msg.Msg = errorx.Decorate(err, funcName)
	msg.FileName = "errors"
	return *msg
}

// 添加监听者
func (l *Listenable) RegisterListener() error {
	if l.queue == nil {
		l.queue = Engine
	}
	return nil
}


// 通知监听者 ： 带参数
func (l *Listenable) NotifyWithArgs(msg Message) {
	if l.queue == nil {
		l.queue = Engine
	}
	l.queue.Update(msg)
}

// 通知监听者： 不带参数
func (l *Listenable) Notify() {
	l.NotifyWithArgs(Message{})
}

func (l *Listenable) NotifyError(err error, funcName string) {
	l.NotifyWithArgs(NewErrorMsg(err, "函数名："+funcName))

}
func InitListenable() *Listenable {
	return &Listenable{}
}
