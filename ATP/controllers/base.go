package controllers

import (
	"github.com/VenmoTools/ATP/libs"
	"github.com/VenmoTools/ATP/models"
	"github.com/astaxie/beego"
	"strings"
	"time"
)

var now = time.Now()

type BaseController struct {
	beego.Controller
	lis libs.Listenable
}

func (this *BaseController) Register() {
	this.lis = *libs.InitListenable()
	this.lis.RegisterListener()
}

func (this *BaseController) Get() {

	this.TplName = "home/home.html"
}

func (this *BaseController) ToIndex() {
	res := libs.SystemInfo(now.Unix())
	cron := new(models.Corn)
	list, _ := cron.GetJobList()
	this.Data["List"] = list
	this.Data["System"] = res
	this.Display("home")
}

func (this *BaseController) Display(folder string) {
	this.Layout = "base/index.html"
	this.LayoutSections = make(map[string]string)
	this.LayoutSections["Header"] = folder + "/header.html"
	this.LayoutSections["Content"] = folder + "/content.html"
	this.LayoutSections["Script"] = folder + "/script.html"
	this.LayoutSections["CSS"] = folder + "/css.html"
	this.Data["xsrf_token"] = this.XSRFToken()
	this.Data["AiHost"] = beego.AppConfig.String("aiHost")

	user := GetSessionUser(this)
	this.Data["user"] = user.UserName
	// 权限控制
	if user.Level == 1 {
		this.Data["Html"] = "<li><a href=\"#\"><i class=\"fa fa-flask\"></i> <span class=\"nav-label\">权限因子</span> <span class=\"fa arrow\"></span> </a><ul class=\"nav nav-second-level collapse\"><li><a href=\"/user/admin/register\">角色添加</a></li></ul></li>"
		this.Data["IsAdministrator"] = "管理员"
		this.Data["display"] = true
	} else if user.Level == 0 {
		this.Data["IsAdministrator"] = "体验用户"
		this.Data["display"] = false
	} else {
		this.Data["IsAdministrator"] = "正式用户"
		this.Data["display"] = false
	}

	this.TplName = "start.html"
}

func GetSessionUser(this *BaseController) (*models.User) {
	// 根据session获取用户名
	uuid := this.GetSession("uuid")
	// 如果用户名为空
	if uuid == nil {
		this.redirect("/user/login")
	}
	user := models.User{UserName: uuid.(string)}
	u, _ := user.GetUser("username")
	return u
}

// 重定向
func (self *BaseController) redirect(url string) {
	self.Redirect(url, 302)
	self.StopRun()
}

//获取用户IP地址
func (self *BaseController) getClientIp() string {
	s := self.Ctx.Request.RemoteAddr
	l := strings.LastIndex(s, ":")
	return s[0:l]
}

//ajax返回 列表
func (self *BaseController) ajaxList(msg interface{}, msgno int, count int64, data interface{}) {
	out := make(map[string]interface{})
	out["code"] = msgno
	out["msg"] = msg
	out["count"] = count
	out["data"] = data
	self.Data["json"] = out
	self.ServeJSON()
	self.StopRun()
}

//ajax返回
func (self *BaseController) ajaxMsg(msg interface{}, msgno int) {
	out := make(map[string]interface{})
	out["status"] = msgno
	out["message"] = msg
	self.Data["json"] = out
	self.ServeJSON()
	self.StopRun()
}
func (self *BaseController) ajaxData(field interface{}) {
	out := make(map[string]interface{})
	out["data"] = field
	self.Data["json"] = out
	self.ServeJSON()
	self.StopRun()
}
