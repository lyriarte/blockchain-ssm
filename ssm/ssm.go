// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type SSMChaincode struct {
}

type Agent struct {
	name		string
	pub			string
}

type State struct {
	ssm			string
	session		string
	current		int
	public		string
	private		map[string]string
}

type Transition struct {
	from		int
	to			int
	agent		string
	action		string
}

type SigningStateMachine struct {
	name		string
	transitions	[]Transition
}


func (t *SSMChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("Signing State Machines Chaincode Init")
	_, args := stub.GetFunctionAndParameters()
	if len(args) != 1 {
		return shim.Error("Incorrect arg count. Expecting 1")
	}

	fmt.Println("SSM Chaincode admins info:", args[0])

	return shim.Success(nil)
}

func (t *SSMChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("Signing State Machines Chaincode Invoke")
	function, args := stub.GetFunctionAndParameters()
	if len(args) != 1 {
		return shim.Error("Incorrect arg count. Expecting 1")
	}

	fmt.Println("SSM Chaincode function:", function)
	fmt.Println("SSM Chaincode args:", args[0])

	return shim.Success(nil)
}

func main() {
	err := shim.Start(new(SSMChaincode))
	if err != nil {
		fmt.Println("Signing State Machines Chaincode init error:", err)
	}
}
