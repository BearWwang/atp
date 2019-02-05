package tests

import (
	"github.com/VenmoTools/ATP/models"
	"testing"
)

func TestMain(m *testing.M) {
	m.Run()
}

func TestElementWorkFlow(t *testing.T) {
	t.Run("Add", TestElement_AddElement)
	t.Run("getlement", TestElement_GetElement)
	t.Run("getElements", TestElement_GetElements)
	t.Run("getById", TestElement_GetElementById)
	t.Run("update", TestElement_UpdateElement)
	t.Run("delte", TestElement_DeleteElement)
}

func TestElement_AddElement(t *testing.T) {
	ele := models.NewElement()
	ele.ElementName = "注册"
	ele.ElementType = "按钮"
	ele.LocatMethod = "xpath"
	ele.LocatValue = "//input[@id='con']"
	_, err := ele.AddElement()
	if err != nil {
		t.Error(err)
	}
}

func TestElement_DeleteElement(t *testing.T) {
	ele := models.NewElement()
	ele.Id = 1
	ele.DeleteElement()
}

func TestElement_GetElementById(t *testing.T) {
	ele := models.NewElement()
	ele.Id = 2
	res, err := ele.GetElementById()
	if err != nil {
		t.Error(err)
	}
	t.Log(res)
}

func TestElement_GetElements(t *testing.T) {
	ele := models.NewElement()
	res, err := ele.GetElements()
	if err != nil {
		t.Error(err)
	}
	for _, v := range res {
		t.Log(v)
	}

}

func TestElement_UpdateElement(t *testing.T) {
	ele := models.NewElement()
	ele.Id = 1
	ele.ElementName = "登录"
	err := ele.UpdateElement("element_name")
	if err != nil {
		t.Error(err)
	}

}

func TestElement_GetElement(t *testing.T) {
	ele := models.NewElement()
	ele.LocatValue = "xpath"
	res, err := ele.GetElement("locat_value")
	if err != nil {
		t.Error(err)
	}
	t.Log(res)
}

func TestElement_ToJson(t *testing.T) {
	ele := models.NewElement()
	ele.Id=2
	res,err := ele.GetElementById()
	if err != nil {
		t.Error(err)
	}

	c,err := models.ToJson(*res)
	if err != nil {
		t.Error(err)
	}

	t.Log(c)
}

func TestElement_ToJsonArray(t *testing.T){
	ele := models.NewElement()
	res,err := ele.GetElements()
	if err != nil {
		t.Error(err)
	}

	c,err := models.TojsonArray(res)
	if err != nil {
		t.Error(err)
	}

	t.Log(c)
}