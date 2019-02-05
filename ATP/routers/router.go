package routers

import (
	"github.com/VenmoTools/ATP/controllers"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/context"
)

func init() {

	//过滤器
	beego.InsertFilter("/[a-z]+/*", beego.BeforeRouter, FilterUser)
	//主页
	base := &controllers.BaseController{}
	// 注册监控
	base.Register()
	beego.Router("/", base)
	beego.Router("/home/", base, "get:ToIndex")
	// 接口测试
	testing := &controllers.TestingController{}
	beego.Router("/testing/interface", testing, "get:Interface")
	beego.Router("/testing/interface/add", testing, "post:AddInterface")
	beego.Router("/testing/interface/run", testing, "post:RunTest")
	// web测试
	webTesting := &controllers.WebTestingController{}
	// 元素仓库
	beego.Router("/testing/web/container", webTesting, "get:ElementContainer")
	beego.Router("/testing/web/container/add", webTesting, "post:AddElement")
	beego.Router("/testing/web/container/del", webTesting, "post:DelElement")
	beego.Router("/testing/web/container/update", webTesting, "post:UpdateElement")

	// 元素动作
	beego.Router("/testing/web/action", webTesting, "get:GetActionList")
	beego.Router("/testing/web/action/add", webTesting, "get:AddAction")
	beego.Router("/testing/web/action/add", webTesting, "post:AddAction")
	beego.Router("/testing/web/action/del", webTesting, "post:DelAction")
	beego.Router("/testing/web/action/update", webTesting, "post:UpdateAction")

	// web测试用例
	beego.Router("/testing/web/cases", webTesting, "get:CaseList")
	beego.Router("/testing/web/cases/add", webTesting, "get:AddCase")
	beego.Router("/testing/web/cases/add", webTesting, "post:AddCase")

	beego.Router("/testing/web/cases/del", webTesting, "post:AddCase")
	beego.Router("/testing/web/cases/update", webTesting, "post:AddCase")

	//开始执行测试Order
	beego.Router("/testing/web/project", webTesting, "get:Chosen")
	beego.Router("/testing/web/order", webTesting, "get:Order")
	beego.Router("/testing/web/order", webTesting, "post:Order")
	beego.Router("/testing/web/key", webTesting, "get:GetKey")

	beego.Router("/testing/web/run", webTesting, "get:RunTest")
	beego.Router("/testing/web/run", webTesting, "post:RunTest")

	// 客户端链接地址
	beego.Router("/testing/web/connection", webTesting, "post:GetClient")
	beego.Router("/testing/web/connection", webTesting, "get:GetClient")


	//渗透测试
	permeation := &controllers.PermeationController{}
	beego.Router("/permeation/env", permeation, "get:Env")

	//项目路由
	product := &controllers.ProductController{}
	beego.Router("/product/", product, "get:List")
	beego.Router("/product/detial", product, "get:Detial")
	beego.Router("/product/add", product, "get:AddProject")
	beego.Router("/product/add", product, "post:AddProject")


	//用户登录
	users := &controllers.UserController{}
	beego.Router("/user/login", users, "get:Login")
	beego.Router("/user/login", users, "post:PostLogin")
	//用户注册
	beego.Router("/user/register", users, "get:Register")
	beego.Router("/user/register", users, "post:Register")
	//管理员注册
	beego.Router("/user/admin/register", users, "get:AdminRegister")
	//退出登录
	beego.Router("/user/logout", users, "get:Logout")

	//忘记密码
	beego.Router("/user/forgot", users, "get:Forgot")
	beego.Router("/user/forgot", users, "post:ForgotPassword")
	//工具
	tools := &controllers.ToolsController{}
	beego.Router("/tools/file", tools, "get:FileManagementView")
	beego.Router("/tools/file/upload", tools, "post:FileManagementUpload")
	beego.Router("/tools/calendar", tools, "get:Calendar")
	beego.Router("/tools/pdf", tools, "get:Pdf")
	beego.Router("/tools/coding", tools, "get:Coding")
	beego.Router("/tools/markdown", tools, "get:MarkDown")

	//邮箱
	mail := &controllers.MailController{}
	beego.Router("/mail/list", mail, "get:List")
	beego.Router("/mail/detail", mail, "get:Detail")

	//容器云
	//beego.Router("/mail/detail", &controllers.MailController{}, "get:Detail")

	//任务调度
	jobs := &controllers.JobsController{}
	beego.Router("/jobs/Jobs", jobs, "get:Jobs")
	beego.Router("/jobs/add", jobs, "post:AddJob")
	beego.Router("/jobs/run", jobs, "post:RunJob")

	//日志
	log := &controllers.LogController{}
	beego.Router("/log/interface", log, "get:LogList")
	beego.Router("/log/interface/detail", log, "get:Log")

	//持续集成
	//beego.Router("/mail/detail", &controllers.MailController{}, "get:Detail")

	//beego.Router("/mail/detail", &controllers.MailController{}, "get:Detail")

}

var FilterUser = func(ctx *context.Context) {
	dd := ctx.Input.Session("uuid")
	if dd != nil {
		curnURI := ctx.Request.RequestURI
		if curnURI != "/user/login" || curnURI != "/user/register" || curnURI != "/user/logout" || curnURI != "/testing/web/connection" {
			ctx.Redirect(302, "/user/login")
		}
	}
}
