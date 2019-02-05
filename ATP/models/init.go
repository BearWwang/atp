package models

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
	"net/url"
)

func init() {
	// 从配置文件读取数据
	dbhost := beego.AppConfig.String("db.host")
	dbport := beego.AppConfig.String("db.port")
	dbuser := beego.AppConfig.String("db.user")
	dbpassword := beego.AppConfig.String("db.password")
	dbname := beego.AppConfig.String("db.name")
	timezone := beego.AppConfig.String("db.timezone")

	//dbhost := "127.0.0.1"
	//dbuser := "root"
	//dbpassword := "admin123"
	//dbport := "3306"
	//dbname := "TE"
	//db.prefix = pp_
	//timezone := "Asia/Shanghai"
	// 如果没有设置端口号使用默认的3306
	if dbport == "" {
		dbport = "3306"
	}
	// 拼接字符串
	dsn := dbuser + ":" + dbpassword + "@tcp(" + dbhost + ":" + dbport + ")/" + dbname + "?charset=utf8"

	// 如果时区为空采取默认时区
	if timezone != "" {
		dsn = dsn + "&loc=" + url.QueryEscape(timezone)
	}

	// 注册数据库
	orm.RegisterDriver("mysql", orm.DRMySQL)
	orm.RegisterDataBase("default", "mysql", dsn)
	orm.RegisterModel(
		new(Testing),
		new(Action),
		new(User),
		new(Cases),
		new(Element),
		new(ProjectModel),
		new(Corn),
		new(BrowserConfig),
		new(TestConfig),
		new(SecurityKey),
	)
	createTable()

	// 如果为开发模式
	if beego.AppConfig.String("runmode") == "dev" {
		orm.Debug = true
	}

}
func createTable() {
	err := orm.RunSyncdb("default", false, false)
	if err != nil {
		panic(err)
	}
}

func TableName(name string) string {
	return beego.AppConfig.String("db.prefix") + name
}
