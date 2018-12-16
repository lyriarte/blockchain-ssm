// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"encoding/json"
)

type Agent struct {
	AgentModel
}

func (self *Agent) Serialize() ([]byte, error) {
	return json.Marshal(self.AgentModel)
}

func (self *Agent) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.AgentModel)
}
