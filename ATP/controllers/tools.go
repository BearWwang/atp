package controllers

import "github.com/astaxie/beego"

type ToolsController struct {
	BaseController
}

func (this *ToolsController) FileManagementView() {

	file := this.GetString("type")
	beego.Info(1234,file)
	this.Display("tools/file")
}

func (this *ToolsController) FileManagementUpload() {
	f,h,err := this.GetFile("filename")
	if err != nil {
		beego.Info(err)
		return
	}
	defer f.Close()
	this.SaveToFile("filename",beego.AppPath+"/files/"+h.Filename)
	this.ajaxMsg("上传成功",TEST_SUCCESS)
}



func (this *ToolsController) Calendar(){
	this.Display("tools/calendar")
}

func (this *ToolsController) Pdf(){
	this.Display("tools/pdf")
}

func (this *ToolsController) Coding(){
	this.Display("tools/coding")
}

func (this *ToolsController) MarkDown(){
	this.Display("tools/md")
}