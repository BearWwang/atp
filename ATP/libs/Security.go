package libs

import (
	"crypto/sha1"
	"fmt"
	"io"
)

func Sha1(str string) (res string) {
	sha := sha1.New()
	io.WriteString(sha, str)
	res = fmt.Sprintf("%x", sha.Sum(nil))
	return
}
