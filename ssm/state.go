// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type State struct {
	StateModel
}

func (self *State) Put(stub shim.ChaincodeStubInterface, key string) error {
	data, err := self.Serialize()
	if (err != nil) {
		return err
	}	
	return stub.PutState(key, data)
}

func (self *State) Get(stub shim.ChaincodeStubInterface, key string) error {
	data, err := stub.GetState(key);
	if (err != nil) {
		return err
	}	
	return self.Deserialize(data)
}

func (self *State) Serialize() ([]byte, error) {
	return json.Marshal(self.StateModel)
}

func (self *State) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.StateModel)
}
