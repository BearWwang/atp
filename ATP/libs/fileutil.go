package libs

import (
	"io/ioutil"
	"os"
	"strings"
)

func SearchFile(path string) (files []os.FileInfo, err error) {
	files, err = ioutil.ReadDir(path)
	if err != nil {
		return
	}
	return
}

func ContainsFile(path string, filename string) bool {
	files, err := SearchFile(path)
	if err != nil {
		return false
	}
	for _, r := range files {
		if !r.IsDir() {
			if strings.Contains(filename, r.Name()) {
				return true
			}
		}
	}
	return false
}
