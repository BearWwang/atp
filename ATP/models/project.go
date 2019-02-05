package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

const (
	ACTIVE = 200
	STOP   = 500
	HEIGHT = 2
	NORMAL = 1
	LOW    = 0
)

type Tag struct {
	Name string
}

type ProjectModel struct {
	Id           int
	ProjectName  string
	Status       int
	Maker        string
	LastUpdate   time.Time `orm:"auto_now;type(datetime)"`
	CreateTime   time.Time `orm:"auto_now_add;type(datetime)"`
	Client       string
	Complete     int
	Icon         string
	ProjectLevel int
	Version      string
	Users        []*User `orm:"reverse(many)"`
}

func (p *ProjectModel) AddProject() (int64, error) {
	return orm.NewOrm().Insert(p);
}

func (p *ProjectModel) GetProjectById() (*ProjectModel, error) {
	return p.GetProject()
}

func (p *ProjectModel) GetProjectByName() (*ProjectModel, error) {
	return p.GetProject("project_name")
}

func (p *ProjectModel) GetProjectByMaker() (*ProjectModel, error) {
	return p.GetProject("maker")
}

func (p *ProjectModel) GetProjectByClient() (*ProjectModel, error) {
	return p.GetProject("client")
}
func (p *ProjectModel) GetProjectByLevel() (*ProjectModel, error) {
	return p.GetProject("project_level")
}

func (p *ProjectModel) GetProject(col ... string) (*ProjectModel, error) {
	err := orm.NewOrm().Read(p, col...)
	if err != nil || err == orm.ErrMissPK || err == orm.ErrNoRows {
		return nil, err
	}
	return p, nil
}

func (p *ProjectModel) GetProjects() ([]*ProjectModel, error) {
	list := make([]*ProjectModel, 0)
	_, err := orm.NewOrm().QueryTable("project_model").All(&list)
	if err != nil {
		return nil, err
	}
	return list, nil
}
