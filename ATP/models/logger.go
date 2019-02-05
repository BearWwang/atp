package models

import "github.com/astaxie/beego/orm"

type Logger struct {
	Id          int    `json:"id"`
	LogerType string `json:"elementName"`
	ElementType string `json:"type"`
	LocatMethod string `json:"method"`
	LocatValue  string `json:"value"`
}

func (this *Logger) AddLog() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *Logger) GetLogById() (*Logger, error) {
	err := orm.NewOrm().Read(this)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *Logger) GetLogs() ([]*Logger, error) {
	ele := make([]*Logger, 0)

	_, err := orm.NewOrm().QueryTable("element").All(&ele)
	if err != nil {
		return nil, err
	}

	return ele, nil
}

func (this *Logger) DeleteLog() error {
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

func (this *Logger) UpdateLog(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

func (this *Logger) GetLog(col ... string) (*Logger, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

