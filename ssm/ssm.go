// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"fmt"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type SSMChaincode struct {
}


func (t *SSMChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	_, args := stub.GetFunctionAndParameters()
	if len(args) != 1 {
		return shim.Error("Incorrect arg count. Expecting 1")
	}

	var err error	
	var admins []Agent
	err = json.Unmarshal([]byte(args[0]), &admins)
	if (err != nil) {
		return shim.Error(err.Error())
	}
	for i := 0; i < len(admins); i++ {
		err = admins[i].Put(stub, "ADMIN_" + admins[i].Name)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	return shim.Success(nil)
}

func (t *SSMChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	
	var err error	
	var admin Agent
	var user Agent
	if function == "register" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		err = user.Deserialize([]byte(args[0]))
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = admin.Get(stub, "ADMIN_" + args[1])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		// TODO validate admin signature
		err = user.Put(stub, "USER_" + user.Name)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "create" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		var ssm SigningStateMachine
		err = ssm.Deserialize([]byte(args[0]))
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = admin.Get(stub, "ADMIN_" + args[1])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		// TODO validate admin signature
		err = ssm.Put(stub, "SSM_" + ssm.Name)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "start" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		var state State
		err = state.Deserialize([]byte(args[0]))
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = admin.Get(stub, "ADMIN_" + args[1])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		// TODO validate admin signature
		err = state.Put(stub, "STATE_" + state.Session)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "perform" {
		if len(args) != 4 {
			return shim.Error("Incorrect arg count.")
		}
		var state State
		err = state.Get(stub, "STATE_" + args[1])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = user.Get(stub, "USER_" + args[2])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		// TODO validate user signature
	}
	
	if function == "session" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		dat, err := stub.GetState("STATE_" + args[0])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	if function == "ssm" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		dat, err := stub.GetState("SSM_" + args[0])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	if function == "user" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		dat, err := stub.GetState("USER_" + args[0])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	if function == "admin" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		dat, err := stub.GetState("ADMIN_" + args[0])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	return shim.Success(nil)
}

func main() {
	err := shim.Start(new(SSMChaincode))
	if err != nil {
		fmt.Println("Signing State Machines Chaincode init error:", err)
	}
}
