// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"encoding/json"
)

type State struct {
	StateModel
}

func (self *State) Serialize() ([]byte, error) {
	return json.Marshal(self.StateModel)
}

func (self *State) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.StateModel)
}
