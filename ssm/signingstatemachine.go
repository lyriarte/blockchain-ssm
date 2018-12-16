// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type SigningStateMachine struct {
	SigningStateMachineModel
}

func (self *SigningStateMachine) Put(stub shim.ChaincodeStubInterface, key string) error {
	data, err := self.Serialize()
	if (err != nil) {
		return err
	}	
	return stub.PutState(key, data)
}

func (self *SigningStateMachine) Get(stub shim.ChaincodeStubInterface, key string) error {
	data, err := stub.GetState(key);
	if (err != nil) {
		return err
	}	
	return self.Deserialize(data)
}

func (self *SigningStateMachine) Serialize() ([]byte, error) {
	return json.Marshal(self.SigningStateMachineModel)
}

func (self *SigningStateMachine) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.SigningStateMachineModel)
}
