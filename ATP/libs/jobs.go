package libs

import (
	"fmt"
	"github.com/gorhill/cronexpr"
	"time"
)

const (
	RUN    = 1
	DELETE = 2
	STOP   = 3
)

type Event struct {
	Type int
	Job  *Job // 任务信息 ，通过发送事件发送任务信息
}

// 任务
type Job struct {
	Name      string
	Cron      string
	JobStatus int
	Testing   RunTesting
}

func NewJob(name string, cron string, test RunTesting) *Job {
	return &Job{
		Name:      name,
		Cron:      cron,
		JobStatus: STOP,
		Testing:   test,
	}
}

// 任务调度计划
type SchedulerPlan struct {
	// 调度任务
	Job      *Job
	Expr     *cronexpr.Expression // 解析好的corn表达式
	NextTime time.Time            // 下次调度计划
}

type Scheduler struct {
	Events     chan *Event
	jobPlanTab map[string]*SchedulerPlan // 调度计划，每个任务执行时间
}

// 处理事件
func (s *Scheduler) EvenHandler(event *Event) {
	var (
		schedulerPlan *SchedulerPlan
		err           error
	)

	switch event.Type {
	case RUN:
		// 如果发生错误忽略该任务
		if schedulerPlan, err = BuildJobPlan(event.Job); err != nil {
			return
		}
		// 如果job的状态不为启动状态忽略该任务
		if event.Job.JobStatus != RUN {
			return
		}
		// 向计划表注册计划
		s.jobPlanTab[event.Job.Name] = schedulerPlan
	case DELETE:
		// 如果任务表存在该任务
		if s.ExistJob(event) {
			//删除任务
			delete(s.jobPlanTab, event.Job.Name)
		}

	}
}

func (s *Scheduler) StopJob(event *Event) bool {
	if s.ExistJob(event) {
		event.Job.JobStatus = STOP
		return true
	}
	return false
}

func (s *Scheduler) ExistJob(event *Event) bool {
	_,ok := s.jobPlanTab[event.Job.Name]
	return ok
}

func (s *Scheduler) Round() (scheduleAfter time.Duration) {
	var (
		schedulerPlan *SchedulerPlan
		now           time.Time
		nearTime      *time.Time
	)

	// 如果任务列表为空睡眠2秒
	if len(s.jobPlanTab) == 0 {
		scheduleAfter = 2 * time.Second
		return
	}

	now = time.Now()
	// 遍历所有任务
	for _, schedulerPlan = range s.jobPlanTab {
		if schedulerPlan.NextTime.Before(now) || schedulerPlan.NextTime.Equal(now) {
			//schedulerPlan.Job.Test.Runs()
			fmt.Println(schedulerPlan.Job.Name + ":执行任务")
			//根据生成的corn表达式获得下次调度时间并更新调度表的时间
			schedulerPlan.NextTime = schedulerPlan.Expr.Next(now)
		}

		// 如果没有记录最近将要发生的任务，或者最近的任务比当前时间还早
		if nearTime == nil || schedulerPlan.NextTime.Before(*nearTime) {
			nearTime = &schedulerPlan.NextTime
		}
	}

	//下次调度时间 (最近的任务时间减去当前时间 就是要休眠的时间)
	scheduleAfter = (*nearTime).Sub(now)
	return
}

//协成调度
func (s *Scheduler) JobLoop() {

	nextJob := s.Round()
	//延时定时器
	timer := time.NewTimer(nextJob)

	// 接受事件
	for {
		select {
		//如果有新的事件
		case jobEvent := <-s.Events:
			//处理事件
			s.EvenHandler(jobEvent)
		case <-timer.C: //最近任务到期
		}
		// 扫描任务列表并调度
		nextJob = s.Round()
		// 重置定时器
		timer.Reset(nextJob)
	}
}

// 推送job事件
func (s *Scheduler) PushJobEvent(event *Event) {
	s.Events <- event
}

var GlobalScheduler *Scheduler

func InitScheduler() {
	GlobalScheduler = &Scheduler{
		Events:     make(chan *Event, 10),
		jobPlanTab: make(map[string]*SchedulerPlan, 10),
	}
	go GlobalScheduler.JobLoop()
}

// 构造任务执行计划
func BuildJobPlan(job *Job) (schedulerPlan *SchedulerPlan, err error) {
	
	exp, err := cronexpr.Parse(job.Cron)
	if err != nil {
		return
	}
	
	// 根据当前的任务生成任务计划表
	schedulerPlan = &SchedulerPlan{
		Expr:     exp,
		NextTime: exp.Next(time.Now()),
		Job:      job,
	}
	return
}
