// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"encoding/json"
)

type SigningStateMachine struct {
	SigningStateMachineModel
}

func (self *SigningStateMachine) Serialize() ([]byte, error) {
	return json.Marshal(self.SigningStateMachineModel)
}

func (self *SigningStateMachine) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.SigningStateMachineModel)
}
