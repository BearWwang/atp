package controllers

import "github.com/astaxie/beego"

type PermeationController struct {
	BaseController
}


func (p *PermeationController) Env() {
	url := "http://" + beego.AppConfig.String("otherhost")
	p.Redirect(url, 301)
}
