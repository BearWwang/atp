package controllers

type MailController struct {
	BaseController
}

func (this *MailController) List(){
	this.Display("mail/list")
}

func (this *MailController) Detail(){
	this.Display("mail/detail")
}