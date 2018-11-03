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

func (t *SSMChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Printf("Signing State Machines Chaincode Init")
	return shim.Success(nil)
}

func (t *SSMChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Printf("Signing State Machines Chaincode Invoke")
	return shim.Success(nil)
}

func main() {
	err := shim.Start(new(SSMChaincode))
	if err != nil {
		fmt.Printf("Signing State Machines Chaincode init error: %s", err)
	}
}
