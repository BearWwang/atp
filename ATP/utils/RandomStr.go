package utils

import (
	"crypto/md5"
	"fmt"
	"io"
	"math/rand"
	"time"
)

func GetRandomString(l int) string {
	str := "0123456789abcdefghijklmnopqrstuvwxyz!@#$%^&*()-=+."
	bytes := []byte(str)
	result := []byte{}
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	for i := 0; i < l; i++ {
		result = append(result, bytes[r.Intn(len(bytes))])
	}
	return string(result)
}


func MD5(password,salt string) string{
	password = password + salt

	// md5加密
	w := md5.New()
	io.WriteString(w, password)   //将str写入到w中
	password = fmt.Sprintf("%x", w.Sum(nil))  //w.Sum(nil)将w的hash转成[]byte格式
	return password
}