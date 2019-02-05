package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

type Corn struct {
	Id          int
	JobName     string
	CronExp     string
	JobCaseId   int
	Ctime       time.Time `orm:"auto_now_add;type(datetime)"`
	Last_update time.Time `orm:"auto_now;type(datetime)"`
	Status      int
}

func (t *Corn) AddJob() (int64, error) {
	return orm.NewOrm().Insert(t)
}

func (t *Corn) GetCaseById() (*Corn, error) {
	err := orm.NewOrm().Read(t)
	if err != nil || err == orm.ErrNoRows || err == orm.ErrMissPK {
		return nil, err
	}
	return t, nil
}

func (t *Corn) UpdateJob(fields ...string) error {
	if _, err := orm.NewOrm().Update(t, fields...); err != nil {
		return err
	}
	return nil
}

func (t *Corn) GetJobList() ([]*Corn, int64) {
	list := make([]*Corn, 0)
	query := orm.NewOrm().QueryTable("corn")
	query.OrderBy().All(&list)
	total, _ := query.Count()
	return list, total
}

func (this *Corn) DeleteJob() error {
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
