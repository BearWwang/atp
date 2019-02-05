package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

type Testing struct {
	Id          int
	Name        string `orm:"column(caseName)"`
	URL         string
	Method      string
	Value       string
	Type        string
	Ctime       time.Time `orm:"auto_now_add;type(datetime)"`
	Last_update time.Time `orm:"auto_now;type(datetime)"`
	Status      int
}

// 添加测试用例
func (t *Testing) AddCase() (int64, error) {
	return orm.NewOrm().Insert(t)
}
func GetTableName() string {
	return "testing"
}

func (t *Testing) GetCaseById() (*Testing, error) {
	err := orm.NewOrm().Read(t)
	if err != nil || err == orm.ErrNoRows || err == orm.ErrMissPK {
		return nil, err
	}
	return t, nil
}

//修改
func (t *Testing) UpdateCase(fields ...string) error {
	if _, err := orm.NewOrm().Update(t, fields...); err != nil {
		return err
	}
	return nil
}

// 获取测试用例列表
func (t *Testing) GetCaseList() ([]*Testing, int64) {
	list := make([]*Testing, 0)
	query := orm.NewOrm().QueryTable(GetTableName())
	query.OrderBy("-id").All(&list)
	total, _ := query.Count()
	return list, total
}
