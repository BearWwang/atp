package controllers

import (
	"github.com/VenmoTools/ATP/models"
	"github.com/VenmoTools/ATP/utils"
	"time"
)

type UserController struct {
	BaseController
}

const (
	SUCCEED    = 1
	USER_FAIED = 101
	FAILED     = 0
)

//显示登录页面
func (this *UserController) Login() {
	// XSRF
	this.XSRFExpire = 7200
	this.Data["xsrf_token"] = this.XSRFToken()
	this.TplName = "user/login.html"
}

// 提交登录
func (this *UserController) PostLogin() {
	username := this.GetString("username")
	password := this.GetString("password")

	o := models.User{UserName: username}
	user, err := o.GetUser("UserName")
	if err != nil {
		this.ajaxMsg("用户名或密码错误", USER_FAIED)

	}
	checkpass := utils.MD5(password, user.Salt)
	if checkpass == user.Password {
		this.SetSession("uuid", username)
		user.LastIp=this.getClientIp()
		times := time.Now().Format("2006 Jan 02 15:04")
		user.LastLogin=times
		this.ajaxMsg("登录成功", SUCCEED)
	} else {
		this.ajaxMsg("用户名或密码错误", USER_FAIED)
	}
}

// 退出登录
func (this *UserController) Logout() {
	this.DelSession("uuid")
	this.redirect("/")
}

//忘记密码页面
func (this *UserController) Forgot() {
	this.TplName = "user/forgot_password.html"
}

// 提交忘记密码
func (this *UserController) ForgotPassword() {
	this.TplName = "user/forgot_password.html"
}

//注册
func (this *UserController) Register() {
	// 如果是POST请求
	if this.Ctx.Request.Method == "POST" {

		UserRegister(this)
	} else {
		this.Data["xsrf_token"] = this.XSRFToken()
		this.TplName = "user/register.html"
	}
}

func GetSession(this *UserController) (*models.User) {
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

func (this *UserController) AdminRegister(){
	this.Display("user/atom")
}



func UserRegister(this *UserController) models.User {

	username := this.Input().Get("username")
	realname := this.Input().Get("realname")
	password := this.Input().Get("password")
	email := this.Input().Get("email")
	phone := this.Input().Get("phone")
	level := this.Input().Get("level")

	if !vaildate(username, realname, password, email, phone) {
		this.ajaxMsg("提交参数不能为空", FAILED)
	}

	var Level int

	checkuser := GetSession(this)

	//检查权限和传递字符
	if checkuser.Level != 1 || level == "" {
		Level = 0
	}

	host := this.getClientIp()
	salt := utils.GetRandomString(16)
	password = utils.MD5(password, salt)

	//用户名检查
	user := models.User{UserName: username, RealName: realname, Password: password, Email: email, Phone: phone, LastIp: host, Salt: salt, Level: Level}
	checkUser, _ := user.GetUser("UserName")

	if checkUser == nil {
		user.AddUser()
		this.ajaxMsg("注册成功", SUCCEED)
	} else if checkUser.UserName == username {
		this.ajaxMsg("账号已存在", USER_FAIED)
	} else {
		this.ajaxMsg("服务器错误", FAILED)
	}
	return user
}

func vaildate(field ...string) bool{
	for _,v:= range field{
		if v == "" {
			return false
		}
	}
	return true
}
