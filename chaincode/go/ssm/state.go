// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"errors"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type State struct {
	StateModel
}

//
// Storable interface implementation
//

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

//
// Serializable interface implementation
//

func (self *State) Serialize() ([]byte, error) {
	return json.Marshal(self.StateModel)
}

func (self *State) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.StateModel)
}

//
// State API implementation
//

func (self *State) Perform(update *State, role string, action string) error {
	// Check if current iteration count passed the limit
	if self.Limit != nil && self.Iteration >= *self.Limit {
		return errors.New("Passed limit iterations count.")
	}
	// Check the proposed update iteration
	if self.Iteration != update.Iteration {
		return errors.New("Invalid iteration number of proposed update.")
	}
	// Set origin transition
	self.Origin = &Transition{self.Current, update.Current, role, action}
	// Increment iteration
	self.Iteration++
	// Update the current state
	self.Current = update.Current
	// Update public and private data
	self.Public = update.Public
	self.Private = update.Private
	// All done
	return nil
}

func (self *State) SetLimit(update *State) error {
	// Check the proposed update iteration
	if self.Iteration != update.Iteration {
		return errors.New("Invalid iteration number of proposed update.")
	}
	// Update iteration limit
	self.Limit = update.Limit 
	// All done
	return nil
}


