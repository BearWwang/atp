package controllers

import (
	"github.com/VenmoTools/ATP/libs"
	"github.com/VenmoTools/ATP/models"
	"github.com/astaxie/beego"
)

type ProductController struct {
	BaseController
}

func (this *ProductController) List() {
	res := new(models.ProjectModel)
	list, err := res.GetProjects()
	if err != nil {
		this.ajaxMsg("获取失败", FAILED)
	}
	this.Data["List"] = list
	this.Display("product/list")
}

func (this *ProductController) Detial() {
	id := this.GetString("id")
	m := models.ProjectModel{Id: libs.ConvertStringToInt(id)}
	res, err := m.GetProjectById()
	if err != nil {
		this.ajaxMsg("失败", FAILED)
	}
	this.Data["product"] = res
	this.Display("product/detial")
}

func (this *ProductController) AddProject() {

	if this.Ctx.Request.Method == "GET" {
		this.Display("product/add")

	} else {

		userList := this.Ctx.Request.Form["Users"]
		productName := this.GetString("productName")
		maker := this.GetString("maker")
		client := this.GetString("client")
		version := this.GetString("version")
		level := this.GetString("level")
		admin := this.GetString("admin")

		beego.Info(userList, productName, maker, client, version, level, admin)

	}

	this.Display("product/add")

}
