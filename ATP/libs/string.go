package libs

import (
	"bufio"
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"golang.org/x/net/html/charset"
	"golang.org/x/text/encoding"
	"golang.org/x/text/encoding/unicode"
	"golang.org/x/text/transform"
	"io"
	"io/ioutil"
	"math/rand"
	"regexp"
	"time"
)

func Md5(buf []byte) string {
	hash := md5.New()
	hash.Write(buf)
	return fmt.Sprintf("%x", hash.Sum(nil))
}

func SizeFormat(size float64) string {
	units := []string{"Byte", "KB", "MB", "GB", "TB"}
	n := 0
	for size > 1024 {
		size /= 1024
		n += 1
	}

	return fmt.Sprintf("%.2f %s", size, units[n])
}

func IsEmail(b []byte) bool {
	var emailPattern = regexp.MustCompile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[a-zA-Z0-9](?:[\\w-]*[\\w])?")
	return emailPattern.Match(b)
}

func Password(len int, pwdO string) (pwd string, salt string) {
	salt = GetRandomString(4)
	defaultPwd := "george518"
	if pwdO != "" {
		defaultPwd = pwdO
	}
	pwd = Md5([]byte(defaultPwd + salt))
	return pwd, salt
}

//生成32位MD5
func MD5(text string) string {
	ctx := md5.New()
	ctx.Write([]byte(text))
	return hex.EncodeToString(ctx.Sum(nil))
}

//生成随机字符串
func GetRandomString(lens int) string {
	str := "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	bytes := []byte(str)
	result := []byte{}
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	for i := 0; i < lens; i++ {
		result = append(result, bytes[r.Intn(len(bytes))])
	}
	return string(result)
}

//转为布尔类型
func ConvertToBool(str string) bool {
	switch str {
	case "1", "t", "on", "true", "True", "y", "yes", "Yes", "YES", "On", "ON", "Y", "T":
		return true
	default:
		return false
	}
}

// 字符编码转换
func StringConvert(t transform.Transformer, str string) (res string, err error) {
	res, _, err = transform.String(t, str)
	return
}

// 字符专为UTF8编码
func StringConvertUtf8(str string) (string, error) {
	return StringConvert(unicode.UTF8.NewEncoder(), str)
}

func CharByteConvert(t transform.Transformer, data []byte) (res []byte, err error) {
	res, _, err = transform.Bytes(t, data)
	return
}

func CharSet(data []byte) (e encoding.Encoding) {
	e, _, _ = charset.DetermineEncoding([]byte(data), "")
	return
}

// 判断字符串是否是 xx开头
func StartWith(str string, char string) bool {
	return str[:1] == char
}

func EndWith(str string, char string) bool {
	return str[len(str)-1:] == char
}

func CutStartChar(str *string) {
	*str = (*str)[1:]
}

func CutLastChar(str *string) {
	*str = (*str)[:len(*str)-1]
}

func CutStartAndLast(str *string) {
	CutLastChar(str)
	CutStartChar(str)
}

func DetermineEncoding(r *bufio.Reader) encoding.Encoding {
	bytes, err := r.Peek(1024)
	if err != nil {
		return unicode.UTF8
	}
	e, _, _ := charset.DetermineEncoding(bytes, "")
	return e
}

func Encode(data io.ReadCloser) (res []byte, err error) {
	reader := bufio.NewReader(data)
	encode := DetermineEncoding(reader)
	read := transform.NewReader(reader, encode.NewEncoder())
	res, err = ioutil.ReadAll(read)

	return
}
