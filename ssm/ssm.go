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

type Agent struct {
	Name		string	`json:"name"`
	Pub			string	`json:"pub"`
}

type State struct {
	Ssm			string	`json:"ssm,omitempty"`
	Session		string	`json:"session"`
	Current		int		`json:"current,omitempty"`
	Public		string	`json:"public,omitempty"`
	Private		map[string]string	`json:"private,omitempty"`
}

type Transition struct {
	From		int		`json:"from"`
	To			int		`json:"to"`
	User		string	`json:"agent"`
	Action		string	`json:"action"`
}

type SigningStateMachine struct {
	Name		string	`json:"name"`
	Transitions	[]Transition	`json:"transitions"`
}


func (t *SSMChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("Signing State Machines Chaincode Init")
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
	admins_info, err := json.Marshal(admins)
	if (err != nil) {
		return shim.Error(err.Error())
	}
	fmt.Println("Admins info:", string(admins_info))
	fmt.Println("")
	for i := 0; i < len(admins); i++ {
		admin_info, err := json.Marshal(admins[i])
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = stub.PutState("ADMIN_" + admins[i].Name, admin_info)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	return shim.Success(nil)
}

func (t *SSMChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("Signing State Machines Chaincode Invoke")
	function, args := stub.GetFunctionAndParameters()
	
	fmt.Println("Function:", function, "args:", len(args))

	var err error	
	var admin Agent
	var user Agent
	if function == "register" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		err = json.Unmarshal([]byte(args[0]), &user)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		user_info, err := json.Marshal(user)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		fmt.Println("User info:", string(user_info))
		fmt.Println("Admin:", args[1], "sign:", args[2])
		dat, err := stub.GetState("ADMIN_" + args[1]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &admin)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = stub.PutState("USER_" + user.Name, user_info)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "create" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		var ssm SigningStateMachine
		err = json.Unmarshal([]byte(args[0]), &ssm)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		ssm_info, err := json.Marshal(ssm)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		fmt.Println("State machine info:", string(ssm_info))
		fmt.Println("Admin:", args[1], "sign:", args[2])
		dat, err := stub.GetState("ADMIN_" + args[1]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &admin)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = stub.PutState("SSM_" + ssm.Name, ssm_info)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "start" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		var state State
		err = json.Unmarshal([]byte(args[0]), &state)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		state_info, err := json.Marshal(state)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		fmt.Println("Session state info:", string(state_info))
		fmt.Println("Admin:", args[1], "sign:", args[2])
		dat, err := stub.GetState("ADMIN_" + args[1]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &admin)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = stub.PutState("STATE_" + state.Session, state_info)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "perform" {
		if len(args) != 4 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Perform action:", args[0])
		var state State
		err = json.Unmarshal([]byte(args[1]), &state)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		state_info, err := json.Marshal(state)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		fmt.Println("Perform state info:", string(state_info))
		fmt.Println("User:", args[2], "sign:", args[3])
		dat, err := stub.GetState("USER_" + args[2]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &user)
		if (err != nil) {
			return shim.Error(err.Error())
		}
	}
	
	if function == "session" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
		var state State
		dat, err := stub.GetState("STATE_" + args[0]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &state)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	if function == "ssm" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
		var ssm SigningStateMachine
		dat, err := stub.GetState("SSM_" + args[0]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &ssm)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	if function == "user" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
		dat, err := stub.GetState("USER_" + args[0]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &user)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	if function == "admin" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
		dat, err := stub.GetState("ADMIN_" + args[0]);
		if (err != nil) {
			return shim.Error(err.Error())
		}
		err = json.Unmarshal(dat, &admin)
		if (err != nil) {
			return shim.Error(err.Error())
		}
		return shim.Success(dat)
	}
	
	fmt.Println()
	return shim.Success(nil)
}

func main() {
	err := shim.Start(new(SSMChaincode))
	if err != nil {
		fmt.Println("Signing State Machines Chaincode init error:", err)
	}
}
