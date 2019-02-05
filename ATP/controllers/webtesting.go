package controllers

import (
	"github.com/VenmoTools/ATP/libs"
	"github.com/VenmoTools/ATP/models"
	"encoding/json"
	"errors"
	"fmt"
	"github.com/astaxie/beego"
	"io/ioutil"
	"strings"
)

type WebTestingController struct {
	BaseController
}

var (
	ClientIp  = ""
	isconnect = false
)

const (
	CONNECT_SUCCESS       = 200
	CONNECT_REJECT        = 509
	CONNECT_FIRST_SUCCESS = 255
)

/*************************Container************************************/

func (this *WebTestingController) ElementContainer() {

	ele := models.NewElement()
	res, err := ele.GetElements()
	if err != nil {
		this.ajaxMsg("获取失败！", TEST_FAILED)
	}
	this.Data["List"] = res
	this.Display("testing/web/container")
}

func (this *WebTestingController) AddElement() {

	elementName := this.Input().Get("elementName")
	elementType := this.Input().Get("elementType")
	method := this.Input().Get("method")
	value := this.Input().Get("value")

	beego.Info(elementType, elementName, method, value)

	ele := models.NewElement()
	ele.ElementName = elementName
	ele.ElementType = elementType
	ele.LocatMethod = method
	ele.LocatValue = value

	if _, err := ele.AddElement(); err != nil {
		this.ajaxMsg("添加数据失败", TEST_FAILED)
	} else {
		this.ajaxMsg("添加成功", TEST_SUCCESS)
	}
}

func (this *WebTestingController) ElementAction() {

	this.Display("testing/web/action")

}

func (this *WebTestingController) DelElement() {
	id := this.Input().Get("id")
	model := models.NewElement()
	model.Id = libs.ConvertStringToInt(id)
	err := model.DeleteElement()
	if err != nil {
		this.ajaxMsg("删除数据失败", TEST_FAILED)
	}

	this.ajaxMsg("删除成功", TEST_SUCCESS)

}

func (this *WebTestingController) UpdateElement() {

	this.ajaxMsg("失败", TEST_FAILED)

}

/*************************Action************************************/

func (this *WebTestingController) AddAction() {

	if this.Ctx.Request.Method == "GET" {
		m := models.Element{}
		list, err := m.GetElements()
		if err != nil {
			this.lis.NotifyError(err, "AddAction()")
			this.ajaxMsg("元素获取失败", TEST_FAILED)
		}

		c := models.Cases{}
		caseList, err := c.GetCases()
		if err != nil {
			this.lis.NotifyError(err, "AddAction()")
			this.ajaxMsg("用例获取失败", TEST_FAILED)
		}

		this.Data["List"] = list
		this.Data["CaseList"] = caseList
		this.Display("testing/web/action/add")
	} else {
		this.ajaxMsg("失败", TEST_FAILED)

	}
}

func (this *WebTestingController) DelAction() {

	this.ajaxMsg("失败", TEST_FAILED)

}

func (this *WebTestingController) UpdateAction() {

	this.ajaxMsg("失败", TEST_FAILED)
}

func (this *WebTestingController) GetActionList() {

	ac := models.Action{}
	AllAc, err := ac.GetActions()
	if err != nil {
		this.lis.NotifyError(err, "GetActionList")
	}

	this.Data["List"] = AllAc

	this.Display("testing/web/action")
}

/*************************selenium客户端************************************/
func (this *WebTestingController) GetClient() {
	if this.Ctx.Request.Method == "GET" {
		isconnect = true
		this.ajaxMsg(this.XSRFToken(), 200);
	}
	key := this.Input().Get("key")
	ClientIp = this.Input().Get("clientIp")
	uuid := this.Input().Get("uuid")
	user := this.Input().Get("user")

	beego.Info("发送", key, ClientIp, uuid, user)

	c := models.SecurityKey{Owner: user}
	check, err := c.GetSecurityKey("owner")

	beego.Info("原有", check.KeyName, check.Key)

	if err != nil || (check.Key != key && check.KeyName == uuid) {
		this.ajaxMsg("拒绝访问", 403)
	}

	if ClientIp != "" && isconnect {
		this.ajaxMsg("链接成功", CONNECT_SUCCESS)
	} else {
		this.ajaxMsg("服务器链接失败", TEST_FAILED)
	}
}

func (this *WebTestingController) GetKey() {
	sess := this.GetSession("uuid")

	key := libs.GetRandomString(16);
	securityKey := libs.Sha1(key + sess.(string))

	check := models.SecurityKey{Owner: sess.(string)}
	c, err := check.GetSecurityKey("owner")

	if c == nil || c.Owner != sess.(string) {
		sk := models.SecurityKey{KeyName: key, Owner: sess.(string), Key: securityKey}
		sk.AddSecurityKey()

		str := "user=%s \n key=%s"

		err = ioutil.WriteFile(beego.AppPath+"/files/key/"+sess.(string)+".txt", []byte(fmt.Sprintf(str, sess.(string), key)), 0666)
		if err != nil {
			this.ajaxMsg("生成Key失败", TEST_FAILED)
		}
	}
	beego.Info(beego.AppPath + "/files/key/" + sess.(string))

	this.Ctx.Output.Download(beego.AppPath+"/files/key/"+sess.(string)+".txt", "key.txt")

}

/*************************TestCase************************************/

func (this *WebTestingController) CaseList() {

	id := this.Input().Get("id") // 项目id
	cases := new(models.Cases)

	if id == "" {

		list, err := cases.GetCases()
		if err != nil {
			this.lis.NotifyError(err, "CaseList()")
			this.ajaxMsg("获取列表出错", TEST_FAILED)
		}
		this.Data["List"] = list

	} else {
		list, err := cases.GetCasesById(id)

		if err != nil {
			this.lis.NotifyError(err, "CaseList()")
			this.ajaxMsg("获取列表出错", TEST_FAILED)
		}

		this.Data["List"] = list
	}

	this.Display("testing/web/Case")
}

func (this *WebTestingController) AddCase() {

	if this.Ctx.Request.Method == "GET" {
		configs := new(models.BrowserConfig)
		list, err := configs.GetBrowserConfigs()
		if err != nil {
			this.lis.NotifyError(err, "AddCase")
		}

		pro := new(models.ProjectModel)
		prolist, err := pro.GetProjects()
		if err != nil {
			this.lis.NotifyError(err, "AddCase")
		}
		this.Data["ProList"] = prolist
		this.Data["TestConfig"] = list

		this.Display("testing/web/Case/add")
	} else {
		caseName := this.Input().Get("caseName")
		caseDescribe := this.Input().Get("caseDescribe")
		functionName := this.Input().Get("functionName")
		needDataProvider := this.Input().Get("needDataProvider")
		assertType := this.Input().Get("assertType")
		productId := this.Input().Get("productId")
		config := this.Input().Get("config")

		afterClass := this.Input().Get("AfterClass")
		beforeClass := this.Input().Get("BeforeClass")
		afterTest := this.Input().Get("AfterTest")
		beforeTest := this.Input().Get("BeforeTest")
		exception := this.Input().Get("Exception")
		dataProvider := this.Input().Get("DataProvider")

		conf := models.TestConfig{
			AfterClass:    libs.ConvertToBool(afterClass),
			BeforeClass:   libs.ConvertToBool(beforeClass),
			AfterTest:     libs.ConvertToBool(afterTest),
			BeforeTest:    libs.ConvertToBool(beforeTest),
			ExceptionTest: libs.ConvertToBool(exception),
			DataProvider:  libs.ConvertToBool(dataProvider),
		}

		_, err := conf.AddTestConfig()

		if err != nil {
			this.lis.NotifyError(err, "AddCase()")
			this.ajaxMsg("添加失败", TEST_FAILED)
		}

		ca := models.Cases{
			CaseName:         caseName,                             // 用例名称
			CaseDescribe:     caseDescribe,                         //用例描述
			FunctionName:     functionName,                         // 生成函数名
			NeedDataProvider: libs.ConvertToBool(needDataProvider), //是否用数据驱动
			AssertType:       assertType,                           // 断言方式
			ProductId:        libs.ConvertStringToInt(productId),   // 所属项目Id
			TestConfig:       conf.Id,                              //使用的测试设置
			BrowserConf:      libs.ConvertStringToInt(config),      // 使用的浏览器设置
		}

		_, err = ca.AddCases()
		if err != nil {
			this.lis.NotifyError(err, "AddCase()")
			this.ajaxMsg("添加失败", TEST_FAILED)
		}
		this.ajaxMsg("添加成功", TEST_SUCCESS)
	}
}

func (this *WebTestingController) DelCase() {
	id := this.Input().Get("id")

	ca := models.Cases{Id: libs.ConvertStringToInt(id)}
	err := ca.DeleteCase()
	if err != nil {
		this.lis.NotifyError(err, "DelCase()")
		this.ajaxMsg("删除失败", TEST_FAILED)
	}
	this.ajaxMsg("删除成功", TEST_SUCCESS)
}

func (this *WebTestingController) UpdateCase() {
	data := this.Ctx.Request.Form
	for k, v := range data {
		beego.Info(k, v)
	}
	this.ajaxMsg("测试", TEST_SUCCESS)
}

// 处理传递的字符串
func handlerString(str string) (string, int) {
	d := strings.Index(str, "|")

	index := strings.Index(str[d+1:], ":")
	caseId := libs.ConvertStringToInt(str[d+1:][index+1:])
	da := str[:d]

	return da, caseId
}

func (this *WebTestingController) Order() {

	if this.Ctx.Request.Method == "POST" {
		data := this.Ctx.Request.Form

		for k, _ := range data {
			da, caseId := handlerString(k)

			if libs.StartWith(da, "[") && libs.EndWith(da, "]") {
				str := string(da)
				libs.CutStartAndLast(&str)
				err := Ordered([]byte(str), caseId)
				if err != nil {
					this.lis.NotifyError(err, "Order")
					this.ajaxMsg("提交失败", TEST_FAILED)
				}
			}
		}
		this.ajaxMsg("成功", SUCCEED)
	} else {
		id := this.Input().Get("caseId")

		c := models.Cases{Id: libs.ConvertStringToInt(id)}
		Case, err := c.GetCaseById()

		if err != nil {
			this.lis.NotifyError(err, "Order")
			this.ajaxMsg("获取测试用例失败", TEST_FAILED)
		}

		TestCase, err := models.MakeTestCase(Case.BrowserConf, libs.ConvertStringToInt(id), Case.TestConfig);
		var cases *models.Case

		for _, v := range TestCase.Cases {
			if v.Id == libs.ConvertStringToInt(id) {
				cases = v
			}
		}

		this.Data["Case"] = cases
		this.Display("testing/web/order")
	}
}

func Ordered(str []byte, cases int) error {

	var data map[string]interface{}

	err := json.Unmarshal(str, &data)
	if err != nil {
		fmt.Println(err)
	}
	for k, v := range data {
		if k == "caseid" {
			if cases != int(v.(float64)) {
				return errors.New(fmt.Sprintf("Case Id not match! got %d except %d", v, cases))
			}
		}
		if k == "children" {
			for i, res := range v.([]interface{}) {
				d := res.(map[string]interface{})
				ac := models.Action{Id: int(d["id"].(float64))}
				newAc, err := ac.GetActionById()
				if err != nil {
					return err
				}
				if newAc.ElementName == d["name"] {
					newAc.Order = uint(i) + 1
					newAc.UpdateAction("order")
				}
			}

		}
	}
	return nil
}

func (this *WebTestingController) Chosen() {

	list, err := new(models.ProjectModel).GetProjects()
	if err != nil {
		this.ajaxMsg("项目获取失败", TEST_FAILED)
	}

	this.Data["List"] = list

	this.Display("testing/web/chosen")
}

/*************************RunTest************************************/
func (this *WebTestingController) RunTest() {

	var (
		product *models.ProjectModel
		err     error
	)

	if this.Ctx.Request.Method == "GET" {

		id := this.Input().Get("productId")
		if id == "" {
			this.ajaxMsg("请检查项目是否存在", TEST_FAILED)
		}
		project := models.ProjectModel{Id: libs.ConvertStringToInt(id)}

		product, err = project.GetProjectById()

		if err != nil {
			this.lis.NotifyError(err, "RunTest")
			this.ajaxMsg("项目获取失败", TEST_FAILED)
		}

		ca := models.Cases{ProductId: product.Id}
		list, err := ca.GetCasesBy("product_id", id)

		if err != nil {
			this.lis.NotifyError(err, "RunTest")
			this.ajaxMsg("获取测试用例错误", TEST_FAILED)
		}

		this.Data["Pro"] = product.ProjectName
		this.Data["List"] = list

		this.Display("testing/web")

	} else if this.Ctx.Request.Method == "POST" {
		if ClientIp == "" {
			this.ajaxMsg("没有检测到selenium客户端，请检查是否运行", TEST_FAILED)
		}
		libs.ClientIp = ClientIp

		id := this.Input().Get("id")
		cases := models.Cases{Id:libs.ConvertStringToInt(id)}

		getCase,err := cases.GetCaseById()
		if err != nil {
			this.lis.NotifyError(err,"WebTestingController.RunTest")
			this.ajaxMsg("获取测试用例错误", TEST_FAILED)

		}
		config := libs.WebTestingConfig{
			Browser:getCase.BrowserConf,
			Product:getCase.ProductId,
			TestConfig:getCase.TestConfig,
		}

		test := libs.WebTesting{
			WebTestingConfig:config,
		}
		err = test.Run()
		if err != nil {
			this.lis.NotifyError(err,"WebTestingController.RunTest")
			this.ajaxMsg("获取测试用例错误", TEST_FAILED)
		}
		this.ajaxMsg("成功", TEST_SUCCESS)
	}
}
