package models

import (
	"encoding/json"
	"github.com/astaxie/beego/orm"
)

type Element struct {
	Id          int    `json:"id"`
	ElementName string `json:"elementName"`
	ElementType string `json:"type"`
	LocatMethod string `json:"method"`
	LocatValue  string `json:"value"`
}

type BrowserConfig struct {
	Id             int
	BrowserName    string
	BrowserVersion string
	Index          string `json:"index"`
}

type TestConfig struct {
	Id            int
	BeforeClass   bool
	AfterClass    bool
	BeforeTest    bool
	AfterTest     bool
	DataProvider  bool
	ExceptionTest bool
}

type Action struct {
	Id            int
	ElementId     int //使用元素的id
	ElementName   string `json:"elementName"`
	ElementMethod string `json:"elementMethod"`
	SendValue     string `json:"SendValue"`
	SelectMethod  string `json:"selectMethod"`
	SelectBy      string `json:"selectBy"`
	Index         string `json:"index"`
	Order         uint
	CaseId        int
}

type Cases struct {
	Id               int
	CaseName         string
	CaseDescribe     string `orm:"size(200)"`
	FunctionName     string
	NeedDataProvider bool
	AssertType       string
	Status           int
	BrowserConf      int
	TestConfig       int
	ProductId        int
}

type Case struct {
	Id               int
	CaseName         string
	FunctionName     string    `json:"functionName"`
	FunctionArgsType string    `json:"functionArgType"`
	AssertType       string    `json:"assertType"`
	Action           []*Action `json:"action",orm:"reverse(many)"`
}

type TestCase struct {
	Id       int
	Browser  *BrowserConfig `json:"BrowserConfig"`
	Test     *TestConfig    `json:"TestConfig"`
	Cases    []*Case        `json:"Cases"`
	Elements []*Element     `json:"Elements"`
}

/*******************Element*******************************/

func (this *Element) AddElement() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *Element) GetElementById() (*Element, error) {
	err := orm.NewOrm().Read(this)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *Element) GetElements() ([]*Element, error) {
	ele := make([]*Element, 0)

	_, err := orm.NewOrm().QueryTable("element").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *Element) DeleteElement() error {
	o := orm.NewOrm()
	err := o.Read(this)
	if err != nil {
		return err
	}

	_, errs := o.Delete(this)
	if errs != nil {
		return errs
	}
	return nil
}

func (this *Element) UpdateElement(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

func (this *Element) GetElement(col ... string) (*Element, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func ToJson(ele Element) (string, error) {
	res, err := json.Marshal(&ele)
	if err != nil {
		return "", err
	}
	return string(res), nil
}

func TojsonArray(ele []*Element) (string, error) {
	res, err := json.Marshal(&ele)
	if err != nil {
		return "", err
	}
	return string(res), nil
}

func NewElement() *Element {
	return &Element{}
}

/*******************BrowserConfig*******************************/

func (this *BrowserConfig) AddBrowserConfig() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *BrowserConfig) GetBrowserConfig(col ...string) (*BrowserConfig, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *BrowserConfig) GetBrowserConfigs() ([]*BrowserConfig, error) {
	ele := make([]*BrowserConfig, 0)

	_, err := orm.NewOrm().QueryTable("browser_config").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *BrowserConfig) GetBcById() (*BrowserConfig, error) {
	return this.GetBrowserConfig("id")
}

func (this *BrowserConfig) UpdateBrowserConfig(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

func (this *BrowserConfig) DeleteBrowserConfig() error {
	o := orm.NewOrm()
	err := o.Read(this)
	if err != nil {
		return err
	}
	_, errs := o.Delete(this)
	if errs != nil {
		return errs
	}
	return nil
}

/*******************Action*******************************/

func (this *Action) AddAction() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *Action) GetAction(col ...string) (*Action, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *Action) GetActionById() (*Action, error) {
	return this.GetAction("id")
}

func (this *Action) GetActions() ([]*Action, error) {
	ele := make([]*Action, 0)

	_, err := orm.NewOrm().QueryTable("action").OrderBy("order").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *Action) GetActionsByCaseId() ([]*Action, error) {
	ele := make([]*Action, 0)

	_, err := orm.NewOrm().QueryTable("action").Filter("case_id", this.CaseId).OrderBy("order").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *Action) UpdateAction(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

func (this *Action) DeleteAction() error {
	o := orm.NewOrm()
	err := o.Read(this)
	if err != nil {
		return err
	}
	_, errs := o.Delete(this)
	if errs != nil {
		return errs
	}
	return nil
}

func (this *Action) GetActionsById(id string) ([]*Action, error) {
	ele := make([]*Action, 0)
	_, err := orm.NewOrm().QueryTable("action").Filter("case_id", id).OrderBy("order").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

/*******************Cases*******************************/

func (this *Cases) AddCases() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *Cases) GetCase(col ...string) (*Cases, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *Cases) GetCaseById() (*Cases, error) {
	return this.GetCase("id")
}

func (this *Cases) GetCases() ([]*Cases, error) {
	ele := make([]*Cases, 0)

	_, err := orm.NewOrm().QueryTable("cases").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}
func (this *Cases) GetCasesBy(col, value string) ([]*Cases, error) {
	ele := make([]*Cases, 0)

	_, err := orm.NewOrm().QueryTable("cases").Filter(col, value).All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *Cases) GetCasesById(id string) ([]*Cases, error) {
	ele := make([]*Cases, 0)

	_, err := orm.NewOrm().QueryTable("cases").Filter("id", id).All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *Cases) DeleteCase() error {
	o := orm.NewOrm()
	err := o.Read(this)
	if err != nil {
		return err
	}

	_, errs := o.Delete(this)
	if errs != nil {
		return errs
	}
	return nil
}

func (this *Cases) UpdateCase(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

/*******************TestConfig*******************************/

func (this *TestConfig) AddTestConfig() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *TestConfig) GetTestConfig(col ...string) (*TestConfig, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *TestConfig) GetTestConfigs() ([]*TestConfig, error) {
	ele := make([]*TestConfig, 0)

	_, err := orm.NewOrm().QueryTable("test_config").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *TestConfig) GetTestConfigById() (*TestConfig, error) {
	return this.GetTestConfig("id")
}

func (this *TestConfig) UpdateTestConfig(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

func (this *TestConfig) DeleteTestConfig() error {
	o := orm.NewOrm()
	err := o.Read(this)
	if err != nil {
		return err
	}

	_, errs := o.Delete(this)
	if errs != nil {
		return errs
	}
	return nil
}

/*******************TestCase*******************************/

func MakeCase(cas int) ([]*Case, []*Element, error) {

	ca := Cases{ProductId: cas} //根据项目id获取对应的case

	tc, err := ca.GetCases() //获取cases

	if err != nil {
		return nil, nil, err
	}

	list := make([]*Case, 0)
	eleList := make([]*Element, 0)

	for _, one := range tc {
		// 创建单个测试用例
		c := new(Case)
		c.Id = one.Id                     // case id
		c.CaseName = one.CaseName         // 用例名称
		c.FunctionName = one.FunctionName // 函数名
		c.AssertType = one.AssertType     // 断言方式

		ac := Action{CaseId: one.Id}    // 每个case对应的执行流程
		c.Action, err = ac.GetActionsByCaseId() // 获取同一个case的执行流程

		if err != nil || len(c.Action) == 0 {
			continue
		}

		for _, e := range c.Action { // 根据caseid获取对应的元素对象，并循环添加

			ele := new(Element) // 新的元素
			ele.ElementName = e.ElementName
			ele.LocatMethod = e.ElementMethod
			ele.LocatValue = e.SendValue

			if err != nil {
				return nil, nil, err
			}
			eleList = append(eleList, ele)
		}
		if err != nil {
			return nil, nil, err
		}

		list = append(list, c)
	}

	return list, eleList, nil
}

// 根据product id生成对应的case
func MakeTestCase(browserConf int, product int, testConfig int) (*TestCase, error) {
	var (
		err     error
		browser BrowserConfig
		conf    TestConfig
	)

	// 浏览器设置
	test := new(TestCase)
	if browserConf != 0 {
		browser = BrowserConfig{Id: browserConf}
	}
	test.Browser, err = browser.GetBcById()

	if err != nil {
		goto ERROR
	}

	// 测试用例总体设置
	conf = TestConfig{Id: testConfig}

	test.Cases, test.Elements, err = MakeCase(product)
	if err != nil {
		goto ERROR
	}

	test.Test, err = conf.GetTestConfigById()

	if err != nil {
		goto ERROR
	}

	return test, nil

ERROR:
	return nil, err

}
