package libs

import (
	"Test/models"
	"encoding/json"
	"errors"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/httplib"
	"io/ioutil"
	"net/http"
	"net/url"
	"strings"
	"time"
)

type RunTesting interface {
	Run() error
}

/**************************接口测试**************************************/
// 接口测试设置
type InterfaceConfig struct {
	Name   string
	Url    string
	Method string
	Param  string
}

// 接口测试运行
type InterfaceTesting struct {
	InterfaceConfig
	Listenable
	StatusCode int
	Result     string
}

// 设置方法
func (c *InterfaceTesting) Config(url, method, param string, name string) {
	c.RegisterListener()
	c.Url = url
	c.Method = strings.ToUpper(method)
	if param != "" {
		c.Param = param
	}
	c.Name = name
}

var ClientIp string
// 执行测试
func (i *InterfaceTesting) Run() error {

	request := httplib.NewBeegoRequest(i.Url, i.Method)

	if i.Param != "" {
		params := strings.Split(i.Param, "&")
		for _, par := range params {
			kv := strings.Split(par, "=")
			if len(kv) == 2 {
				request.Param(kv[0], kv[1])
			} else {
				continue
			}
		}
	}

	res, err := request.DoRequest()

	if err != nil {
		i.NotifyError(err, "InterfaceTesting.Run()")
		return err
	}

	i.TestingResult(res, i.Name+".md")
	i.StatusCode = res.StatusCode
	response, err := ioutil.ReadAll(res.Body)

	if err != nil {
		i.NotifyError(err, "Run()")
		return err
	}
	i.Result = string(response)
	return nil
}

func GetParam(data http.Header) (res []string) {
	for k, v := range data {
		res = append(res, fmt.Sprintf("|%s|%s|", k, v))
	}
	return
}

func GetRequestParam(data url.Values) (res []string) {
	for k, v := range data {
		res = append(res, fmt.Sprintf("|%s|%s|", k, v[0]))
	}
	return
}

func (c *InterfaceTesting) TestingResult(res *http.Response, fileName string) {
	now := time.Now().Format("2006-01-02 15:04:05")
	r, err := NewRender(TEMPLATE_FILE_DIR+"/interfaceTest.md", INTERFACE_TEST_DIR+fileName)

	if err != nil {
		c.NotifyError(err, "TestingResult()")
	}
	if r == nil {
		c.NotifyError(errors.New("null pointer"), "TestingResult()")
		return
	}
	r.Data["Url"] = res.Request.URL
	r.Data["Time"] = now

	r.Data["RequestHeader"] = GetParam(res.Header)
	r.Data["ResponseHeader"] = GetParam(res.Request.Header)
	method := res.Request.Method
	if method == http.MethodGet {
		r.Data["RequestParam"] = GetRequestParam(res.Request.URL.Query())
	} else if method == http.MethodPost {
		r.Data["RequestParam"] = GetRequestParam(res.Request.PostForm)
	}
	datas, err := ioutil.ReadAll(res.Body)
	if err == nil {
		r.Data["ResponseBody"] = string(datas)
	} else {
		r.Data["ResponseBody"] = ""
	}
	r.StartRender()

}

/**************************Web测试**************************************/

type WebTesting struct {
	WebTestingConfig
	Listenable
	StatusCode int
	Result     string
}

type WebTestingConfig struct {
	Browser    int
	Product    int
	TestConfig int
}

func (w *WebTesting) Run() error {
	var (
		err   error
		cases *models.TestCase
	)

	if cases, err = models.MakeTestCase(w.Browser, w.Product, w.TestConfig); err != nil {
		w.NotifyError(err, "WebTestingConfig.Run")
		return err
	}
	res, err := json.Marshal(cases);
	if err != nil {
		w.NotifyError(err, "WebTestingConfig.Run")
		return err
	}

	beego.Info(string(res))

	request := httplib.NewBeegoRequest("http://"+ClientIp+"/api", "POST")
	request.Body(string(res))
	request.SetUserAgent("VenmoSnake")
	response, err := request.DoRequest()
	if err != nil {
		w.NotifyError(err, "WebTestingConfig.Run")
	}

	data, err := Encode(response.Body)

	//var res map[string]interface{}

	mp := make(map[string]interface{})
	json.Unmarshal(data,&mp)
	fmt.Println(string(data))

	for k,v :=  range mp{
		if k == "status" {
			if int(v.(float64)) == 200 {
				return nil
			}
			return errors.New("测试失败")
		}
	}



	return nil
}
