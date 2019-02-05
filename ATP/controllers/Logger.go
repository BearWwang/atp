package controllers

import (
	"github.com/VenmoTools/ATP/libs"
	"github.com/VenmoTools/ATP/models"
)

type LogController struct {
	BaseController
}

func (l *LogController) Log() {
	id := l.Input().Get("id")
	filename := id + ".md"

	if !libs.ContainsFile("/"+libs.INTERFACE_TEST_DIR, filename) {
		l.redirect("/log/interface")
	}
	l.Data["Md"] = "log/interface/"+filename
	l.Display("log/interfaceLog/detail")

}

func (l *LogController) LogList() {
	model := models.Testing{}
	list,_ := model.GetCaseList()

	l.Data["List"] = list

	l.Display("log/interfaceLog/list")
}
