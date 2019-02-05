package models

import (
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
	"time"
)

type User struct {
	Id         int
	Project    *ProjectModel `orm:"rel(fk)"`
	UserName   string
	RealName   string
	Password   string
	Phone      string
	Email      string
	Salt       string
	LastLogin  string
	LastIp     string
	Level      int
	CreateTime time.Time `orm:"auto_now_add;type(datetime)"`
	UpdateTime time.Time `orm:"auto_now;type(datetime)"`
}

func (u *User) AddUser() (int64, error) {
	return orm.NewOrm().Insert(u)
}

func (u *User) UpdateUser(field ...string) (error) {
	_, err := orm.NewOrm().Update(u, field...)
	if err != nil {
		return err
	}
	return nil
}

func (u *User) GetUser(cols ...string) (*User, error) {
	err := orm.NewOrm().Read(u, cols ...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}
	return u, nil
}

func (u *User) GetUserList() ([]*User, error) {
	list := make([]*User, 0)

	_, err := orm.NewOrm().QueryTable("user").All(&list)
	if err != nil {
		return nil, err
	}
	return list, nil
}

func (u *User) DeleteUser() error {
	o := orm.NewOrm()
	err := o.Read(u)
	if err != nil {
		return err
	}
	_, errs := o.Delete(u)

	if errs != nil {
		return errs
	}
	return nil
}

func (u *User) GetProject() (*ProjectModel, error) {
	m := ProjectModel{}
	_, err := orm.NewOrm().QueryTable("project_model").Filter("user", 1).RelatedSel().All(&m)
	if err != nil {
		return nil, err
	}
	return &m, nil
}

func (u *User) AddProject(project *ProjectModel) error {
	if  u.Project == nil {
		u.Project = project
		er := u.UpdateUser("project_model")
		if er != nil {
			return er
		}
	}
	return nil
}
