package controllers

import (
	"github.com/VenmoTools/ATP/libs"
	"github.com/VenmoTools/ATP/models"
	"github.com/astaxie/beego"
	"strconv"
)

type TestingController struct {
	BaseController
}

const (
	TEST_FAILED  = 2
	TEST_SUCCESS = 1
)

func (this *TestingController) InterfaceTest() {
	this.Display("testing/interface")
}

func (this *TestingController) Interface() {
	list, _ := new(models.Testing).GetCaseList()
	this.Data["List"] = list
	this.Display("testing/interface")
}

func (this *TestingController) AddInterface() {

	name, err := libs.StringConvertUtf8(this.Input().Get("names"))
	addr, err := libs.StringConvertUtf8(this.Input().Get("addr"))
	method, err := libs.StringConvertUtf8(this.Input().Get("method"))
	value, err := libs.StringConvertUtf8(this.Input().Get("value"))
	types, err := libs.StringConvertUtf8(this.Input().Get("type"))

	if err != nil {
		this.lis.NotifyError(err, "AddInterface")
		this.ajaxMsg("添加数据失败", TEST_FAILED)
	}

	cases := models.Testing{
		Name:   name,
		URL:    addr,
		Method: method,
		Value:  value,
		Type:   types,
		Status: 0,
	}
	if _, err := cases.AddCase(); err != nil {
		this.lis.NotifyError(err, "AddInterface()")
		this.ajaxMsg("添加数据失败", TEST_FAILED)
	}
	this.ajaxMsg("succeed", TEST_SUCCESS)
}

func (this *TestingController) RunTest() {
	id := this.Input().Get("id")
	beego.Info(id)
	m := models.Testing{Id: libs.ConvertStringToInt(id)}
	cases, err := m.GetCaseById()
	if err != nil {
		this.lis.NotifyError(err, "RunTest()")
		this.ajaxMsg("测试失败！详情请查看记录", TEST_FAILED)
	}
	// 新建测试
	test := new(libs.InterfaceTesting)
	// 设置测试用例
	beego.Info(strconv.Itoa(cases.Id))
	test.Config(cases.URL, cases.Method, cases.Value, strconv.Itoa(cases.Id))

	// 开始测试
	err = test.Run()
	if err != nil || test.StatusCode > 300 {
		this.lis.NotifyError(err, "RunTest()")
		cases.Status = TEST_FAILED
		cases.UpdateCase("status")
		this.ajaxMsg("测试失败！详情请查看记录", TEST_FAILED)
	} else {
		cases.Status = TEST_SUCCESS
		cases.UpdateCase("status")
		this.ajaxMsg("测试成功！", TEST_SUCCESS)
	}
}
