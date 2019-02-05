package controllers

import (
	"github.com/VenmoTools/ATP/libs"
	"github.com/VenmoTools/ATP/models"
)

const (
	RUNNING = 0
	STOP    = 1
	CANCEL  = 2
)

type JobsController struct {
	BaseController
}

func (this *JobsController) Jobs() {

	model := new(models.Testing)
	list, _ := model.GetCaseList()
	job := new(models.Corn)
	jobs, _ := job.GetJobList()

	this.Data["Jobs"] = jobs
	this.Data["List"] = list

	this.Display("jobs")
}

func (this *JobsController) AddJob() {
	id := this.Input().Get("JobId")
	name := this.Input().Get("JobName")
	cron := this.Input().Get("Cron")
	newId := libs.ConvertStringToInt(id)
	res := models.Testing{Id: newId}
	cases, err := res.GetCaseById()

	// 如果没有指定名称默认使用测试名称
	if name == "" {
		name = cases.Name + "_任务"
	}

	mo := models.Corn{JobName: name, CronExp: cron, JobCaseId: newId}
	mo.AddJob()

	if err != nil {
		this.ajaxMsg("数据错误", FAILED)
	}
	test := new(libs.InterfaceTesting)
	test.Config(cases.URL, cases.Method, cases.Value, string(cases.Id))

	job := libs.NewJob(name, cron, test)
	job.JobStatus = libs.RUN

	libs.GlobalScheduler.PushJobEvent(&libs.Event{Type: libs.RUN, Job: job})

	this.ajaxMsg("添加成功", SUCCEED)
}

func (this *JobsController) RunJob() {
	id := this.Input().Get("id")
	res := models.Corn{Id: libs.ConvertStringToInt(id)}
	j, err := res.GetCaseById()
	if err != nil {
		this.ajaxMsg("获取出错", FAILED)
	}
	j.Status = RUNNING
	j.UpdateJob("id")
	this.ajaxMsg("启动成功", SUCCEED)
}

func (this *JobsController) StopJob() {
	id := this.Input().Get("id")
	res := models.Corn{Id: libs.ConvertStringToInt(id)}
	j, err := res.GetCaseById()
	if err != nil {
		this.ajaxMsg("获取出错", FAILED)
	}
	libs.GlobalScheduler.PushJobEvent(&libs.Event{Type: libs.STOP, Job: nil})
	j.Status = STOP
	this.ajaxMsg("停止成功", SUCCEED)
}
