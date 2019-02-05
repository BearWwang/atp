package tests

import (
	"github.com/VenmoTools/ATP/models"
	"testing"
)


func TestCaseWorkFlow(t *testing.T) {
	//t.Run("tablename",TestGetTableName)
	t.Run("addCase",TestTesting_AddCase)
	t.Run("getCase",TestTesting_GetCaseById)
	t.Run("getList",TestTesting_GetCaseList)
	t.Run("update",TestTesting_UpdateCase)
}

func TestGetTableName(t *testing.T) {
	name := models.GetTableName()
	if name != "Testing" {
		 t.Error("名称错误")
	}
}

func TestTesting_AddCase(t *testing.T) {
	cases := models.Testing{Name:"接口测试用例",Method:"Post",URL:"http://www.baidu.com",Value:"user=admin&pass=123",Status:0}
	if _,err:=cases.AddCase();err != nil{
		t.Error(err)
	}
}

func TestTesting_GetCaseById(t *testing.T) {
	ca := models.Testing{Id:5}
	res,err := ca.GetCaseById()
	if err != nil {
		t.Error(err)
	}
	t.Log(res)
}

func TestTesting_GetCaseList(t *testing.T) {
	ca,count := new(models.Testing).GetCaseList()
	if len(ca) < 0 {
		t.Error(ca)
	}
	for _,v := range ca{
		t.Log(v)
	}
	t.Log(count)
}

func TestTesting_UpdateCase(t *testing.T) {
	ca := models.Testing{Id:6,Name:"APP测试"}
	ca.UpdateCase("Name")
	b,err := ca.GetCaseById()
	if err !=nil {
		t.Log(err)
	}
	t.Log(b)
}

func TestUser_GetProject(t *testing.T) {
	user := new(models.User)
	res,err := user.GetProject()
	if err != nil {
		t.Error(err)
	}
	t.Log(res.ProjectName)
}

func TestUser_AddProject(t *testing.T) {
	user := new(models.User)
	user.UserName = "Test"
	res,err := user.GetUser("user_name")
	if err != nil {
		t.Error(err)
	}

	model := models.ProjectModel{Id:1}
	project ,err := model.GetProjectById()
	if err != nil {
		t.Error(err)
	}
	t.Log(project.ProjectName)
	res.AddProject(project)
}