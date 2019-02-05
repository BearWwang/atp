package models

import "github.com/astaxie/beego/orm"

type SecurityKey struct {
	Id      int
	KeyName string
	Owner   string
	Key     string
}

func (this *SecurityKey) AddSecurityKey() (int64, error) {
	return orm.NewOrm().Insert(this)
}

func (this *SecurityKey) GetSecurityKeyById() (*SecurityKey, error) {
	err := orm.NewOrm().Read(this)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}

func (this *SecurityKey) DeleteSecurityKey() error {
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

func (this *SecurityKey) UpdateSecurityKey(cols ...string) error {
	_, err := orm.NewOrm().Update(this, cols...)
	if err != nil {
		return err
	}
	return nil
}

func (this *SecurityKey) GetSecurityKey(col ... string) (*SecurityKey, error) {
	err := orm.NewOrm().Read(this, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}

	return this, nil
}
