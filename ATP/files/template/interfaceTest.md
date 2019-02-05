# 接口测试结果

## 请求概要
|测试时间|测试URL|测试结果|
|--------|:-----:|:-----:|
|{{.Time}}|{{.Url}}|`PASS`

##详情
### 请求
***
#### 请求头
|字段名|字段值|
|--------|:-----:|{{range $item := .RequestHeader}}
{{$item}}{{end}}

{{if .RequestParam}}
#### 请求参数
|参数|请求值|
|--------|:-----:|{{range $item := .RequestParam}}
{{$item}}{{end}}
{{end}}

### 响应

***
#### 响应头
|字段名|字段值|
|--------|:-----:|{{range $item := .ResponseHeader}}
{{$item}}{{end}}

### 响应值


<div class="col-md-12" >
    <div class="ibox ">
        <div class="ibox-content dot-ellipsis truncate fh-200" id="response">
            <p>
{{.ResponseBody}}
            </p>
        </div>
    </div>
</div>