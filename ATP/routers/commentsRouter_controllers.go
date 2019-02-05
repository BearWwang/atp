package routers

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/context/param"
)

func init() {

	beego.GlobalControllerRouter["Test/controllers:UserController"] = append(beego.GlobalControllerRouter["Test/controllers:UserController"],
		beego.ControllerComments{
			Method:           "Home",
			Router:           `/home/`,
			AllowHTTPMethods: []string{"get"},
			MethodParams:     param.Make(),
			Filters:          nil,
			Params:           nil})

	beego.GlobalControllerRouter["Test/controllers:UserController"] = append(beego.GlobalControllerRouter["Test/controllers:UserController"],
		beego.ControllerComments{
			Method:           "Login",
			Router:           `/login/`,
			AllowHTTPMethods: []string{"get"},
			MethodParams:     param.Make(),
			Filters:          nil,
			Params:           nil})

	beego.GlobalControllerRouter["Test/controllers:UserController"] = append(beego.GlobalControllerRouter["Test/controllers:UserController"],
		beego.ControllerComments{
			Method:           "Get",
			Router:           `/test/`,
			AllowHTTPMethods: []string{"get"},
			MethodParams:     param.Make(),
			Filters:          nil,
			Params:           nil})

}
