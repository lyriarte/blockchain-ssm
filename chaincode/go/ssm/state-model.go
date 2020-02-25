// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

type StateModel struct {
	ObjectType	string	`json:"docType,omitempty"`
	Ssm			string	`json:"ssm,omitempty"`
	Session		string	`json:"session"`
	Iteration	int		`json:"iteration"`
	Limit		*int	`json:"limit,omitempty"`
	Roles		map[string]string	`json:"roles"`
	Current		int		`json:"current"`
	Origin		*Transition	`json:"origin,omitempty"`
	Public		interface{}	`json:"public,omitempty"`
	Private		map[string]string	`json:"private,omitempty"`
}
