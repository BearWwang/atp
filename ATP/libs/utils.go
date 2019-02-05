package libs

import (
	"github.com/astaxie/beego"
	"strconv"
)

func ConvertStringToInt(str string) int {
	res, err := strconv.ParseInt(str, 10, 0)
	if err != nil {
		beego.Error(str, "can not convert to int")
		return -1
	}
	return int(res)
}

