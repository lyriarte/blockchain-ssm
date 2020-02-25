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

//
// Storable interface implementation
//

func (self *SigningStateMachine) Put(stub shim.ChaincodeStubInterface, key string) error {
	self.SigningStateMachineModel.ObjectType = "ssm"
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
	err = self.Deserialize(data)
	if (err != nil) {
		return err
	}	
	self.SigningStateMachineModel.ObjectType = ""
	return err
}

//
// Serializable interface implementation
//

func (self *SigningStateMachine) Serialize() ([]byte, error) {
	return json.Marshal(self.SigningStateMachineModel)
}

func (self *SigningStateMachine) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.SigningStateMachineModel)
}

//
// SigningStateMachine API implementation
//

func (self *SigningStateMachine) NextState(from int, role string, action string) int {
	// Iterate through transition list
	for _, trans := range self.SigningStateMachineModel.Transitions {
		// If we found a matching transition
		if from == trans.From && role == trans.Role && action == trans.Action {
			// Return the destination state
			return trans.To
		}
	} 
	// No valid transition was found
	return -1
}
