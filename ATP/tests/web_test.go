package tests

import (
	"github.com/VenmoTools/ATP/models"
	"encoding/json"
	"testing"
)


func TestCases_AddCases(t *testing.T) {
	ca := new(models.Cases)
	ca.FunctionName = "test"
	ca.CaseDescribe = "测试函数"
	ca.AssertType = "equal"
	ca.NeedDataProvider = false
	ca.AddCases()
}

func TestAction_AddAction(t *testing.T) {
	ele := new(models.Element)
	ele.Id = 2
	el, err := ele.GetElementById()
	if err != nil {
		t.Error(err)
	}

	action := new(models.Action)
	action.CaseId = 1
	action.ElementId = el.Id
	action.ElementMethod = el.LocatMethod
	action.SendValue = el.LocatValue
	action.ElementName = el.ElementName

	action.AddAction()

}

func TestBrowserConfig_AddBrowserConfig(t *testing.T) {
	browser := new(models.BrowserConfig)
	browser.BrowserName = "firefox"
	browser.BrowserVersion = "v0.23"
	browser.Index = "http://www.baidu.com"
	browser.AddBrowserConfig()
}

func TestTestConfig_AddTestConfig(t *testing.T) {
	config := new(models.TestConfig)
	config.AfterClass = false
	config.AfterTest = true
	config.BeforeClass = false
	config.BeforeTest = true
	config.DataProvider = false
	config.ExceptionTest = false
	config.AddTestConfig()
}

func TestMakeTestCase(t *testing.T) {
	ca,el,err := models.MakeCase(1)
	if err != nil {
		t.Error(err)
	}

	for _,c := range ca{
		t.Log(c.FunctionName)
	}

	for _,e := range el {
		t.Log(e.ElementName)
	}

}


func TestMakeCase(t *testing.T) {
	test,err := models.MakeTestCase(1,1,1)
	if err != nil {
		t.Error(err)
	}
	res,err := json.Marshal(&test)
	if err != nil {
		t.Error(err)
	}
	t.Log(string(res))




	for _,v := range test.Cases{
		t.Log(v.Id)
		for _,r := range v.Action{
			t.Log(r)
		}
	}


}

func Ordered(data string){
	j :=json.Decoder{}
	j.Decode(data)
}