package main

import (
	"Test/libs"
	_ "Test/routers"
	"github.com/astaxie/beego"
	"html/template"
	"net/http"
)

func page_not_found(rw http.ResponseWriter, r *http.Request) {
	t, _ := template.New("404.html").ParseFiles(beego.BConfig.WebConfig.ViewsPath + "/error/404.html")
	data := make(map[string]interface{})
	data["content"] = "找不到网页"
	t.Execute(rw, data)
}

func internal_server_error(rw http.ResponseWriter, r *http.Request) {
	t, _ := template.New("500.html").ParseFiles(beego.BConfig.WebConfig.ViewsPath + "/error/500.html")
	data := make(map[string]interface{})
	data["content"] = "内部服务器错误"
	t.Execute(rw, data)
}

func main() {
	beego.ErrorHandler("404", page_not_found)
	beego.ErrorHandler("500", internal_server_error)
	beego.SetStaticPath("/static", "static")
	beego.SetStaticPath("/img", "img")
	beego.SetStaticPath("/files", "files")
	libs.InitScheduler()

	beego.Run()
}
