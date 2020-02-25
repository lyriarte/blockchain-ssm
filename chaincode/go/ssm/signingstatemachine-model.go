// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

type Transition struct {
	From		int		`json:"from"`
	To			int		`json:"to"`
	Role		string	`json:"role"`
	Action		string	`json:"action"`
}

type SigningStateMachineModel struct {
	ObjectType	string	`json:"docType,omitempty"`
	Name		string	`json:"name"`
	Transitions	[]Transition	`json:"transitions"`
}


