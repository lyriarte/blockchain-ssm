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

	return shim.Success(nil)
}

func (t *SSMChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("Signing State Machines Chaincode Invoke")
	function, args := stub.GetFunctionAndParameters()
	
	fmt.Println("Function:", function, "args:", len(args))

	var err error	
	if function == "register" {
		if len(args) != 3 {
			return shim.Error("Incorrect arg count.")
		}
		var user Agent
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
	}
	
	if function == "session" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
	}
	
	if function == "ssm" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
	}
	
	if function == "user" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
	}
	
	if function == "admin" {
		if len(args) != 1 {
			return shim.Error("Incorrect arg count.")
		}
		fmt.Println("Identifier:", args[0])
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
