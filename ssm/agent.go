// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type Agent struct {
	AgentModel
}

func (self *Agent) Put(stub shim.ChaincodeStubInterface, key string) error {
	data, err := self.Serialize()
	if (err != nil) {
		return err
	}	
	return stub.PutState(key, data)
}

func (self *Agent) Get(stub shim.ChaincodeStubInterface, key string) error {
	data, err := stub.GetState(key);
	if (err != nil) {
		return err
	}	
	return self.Deserialize(data)
}

func (self *Agent) Serialize() ([]byte, error) {
	return json.Marshal(self.AgentModel)
}

func (self *Agent) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.AgentModel)
}
