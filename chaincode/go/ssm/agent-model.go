// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

type AgentModel struct {
	ObjectType	string	`json:"docType,omitempty"`
	Name		string	`json:"name"`
	Pub			string	`json:"pub"`
}

