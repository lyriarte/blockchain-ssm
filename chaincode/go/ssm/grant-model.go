// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2019 
// License: Apache-2.0

package main

type Credit struct {
	Amount		int		`json:"amount"`
}

type GrantModel struct {
	ObjectType	string	`json:"docType,omitempty"`
	User		string	`json:"user"`
	Iteration	int		`json:"iteration"`
	Credits		map[string]*Credit	`json:"credits"`
}
