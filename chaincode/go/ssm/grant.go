// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2019 
// License: Apache-2.0

package main

import (
	"errors"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type Grant struct {
	GrantModel
}

//
// Storable interface implementation
//

func (self *Grant) Put(stub shim.ChaincodeStubInterface, key string) error {
	self.GrantModel.ObjectType = "grant"
	data, err := self.Serialize()
	if (err != nil) {
		return err
	}	
	return stub.PutState(key, data)
}

func (self *Grant) Get(stub shim.ChaincodeStubInterface, key string) error {
	data, err := stub.GetState(key);
	if (err != nil) {
		return err
	}	
	err = self.Deserialize(data)
	if (err != nil) {
		return err
	}	
	self.GrantModel.ObjectType = ""
	return err
}

//
// Serializable interface implementation
//

func (self *Grant) Serialize() ([]byte, error) {
	return json.Marshal(self.GrantModel)
}

func (self *Grant) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.GrantModel)
}

//
// Grant API implementation
//

func (self *Grant) SetCredits(update *Grant) error {
	// Check the proposed update iteration
	if self.Iteration != update.Iteration {
		return errors.New("Invalid iteration number of proposed update.")
	}
	// Check the proposed update user
	if self.User != update.User {
		return errors.New("Invalid user for proposed update.")
	}
	// Check credits for authorized api
	for api := range update.Credits {
		if api != "register" && api != "create" && api != "start" {
			return errors.New("Unallowed API grant request")
		}
	}
	// Increment iteration
	self.Iteration++
	// Update the user credits
	self.Credits = update.Credits
	// All done
	return nil
}

func (self *Grant) ApiGrant(user string, api string) error {
	// Check user
	if self.User != user {
		return errors.New("Invalid user for api grant.")
	}
	// Check api credit
	if self.Credits[api] == nil || self.Credits[api].Amount <= 0 {
		return errors.New("User has no api credits.")
	}
	// Decrement the user credits for this api
	self.Credits[api].Amount--
	// Granted
	return nil
}


