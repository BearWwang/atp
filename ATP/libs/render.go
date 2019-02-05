package libs

import (
	"github.com/astaxie/beego"
	"html/template"
	"os"
)

var (
	DOCUMENT_DIR       = beego.AppPath + "/files"
	INTERFACE_TEST_DIR = DOCUMENT_DIR + "/log/interface/"
	TEMPLATE_FILE_DIR  = DOCUMENT_DIR + "/template"
)
/**
	测试结果渲染
 */
type Render struct {
	execute *template.Template
	Data    map[string]interface{}
	render  func(filename string) (error, *os.File)
	Path    string
}

/**
	主方法，返回错误信息
 */
func (r *Render) StartRender() (error) {
	err, f := r.render(r.Path)
	if err != nil {
		return err
	}
	err = r.execute.Execute(f, r.Data)
	if err != nil {
		return err
	}
	return nil
}

func NewRender(tempFile, saveFile string) (r *Render, err error) {
	r = new(Render)
	r.Path = saveFile
	r.execute, err = template.ParseFiles(tempFile)
	r.render = Save
	r.Data = make(map[string]interface{})
	return
}

func Save(filename string) (err error, file *os.File) {
	file, err = os.OpenFile(filename, os.O_CREATE|os.O_RDWR, 0666)
	if err != nil {
		return
	}
	return
}
