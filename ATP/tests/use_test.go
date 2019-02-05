package tests

import (
	"github.com/VenmoTools/ATP/models"
	"testing"
)



func TestUserWorkFlow(t *testing.T){
	t.Run("add",TestUser_AddUser)
	t.Run("get",TestUser_GetUserById)
	t.Run("list",TestUser_GetUserList)
	t.Run("update",TestUser_UpdateUser)
	t.Run("del",TestUser_DeleteUser)
}


func TestUser_AddUser(t *testing.T) {
	user := models.User{UserName:"admin",Password:"123",RealName:"Snake"}
	if _,err := user.AddUser(); err !=nil{
		t.Error(err)
	}
}

func TestUser_GetUserById(t *testing.T) {
	user :=models.User{Id:1}
	_,err := user.GetUser()
	if err != nil {
		t.Error(err)
	}


}
func TestUser_GetUserList(t *testing.T) {

	list,err := new(models.User).GetUserList()
	if err != nil {
		t.Error(err)
	}
	for _,i := range list{
		t.Log(i)
	}

}
func TestUser_UpdateUser(t *testing.T) {
	user := models.User{Id:2,UserName:"cc"}
	err := user.UpdateUser("UserName")
	if err != nil {
		t.Error(err)
	}

}

func TestUser_DeleteUser(t *testing.T) {
	user := new(models.User)
	user.Id = 3
	user.DeleteUser()
}