// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

type StateModel struct {
	Ssm			string	`json:"ssm,omitempty"`
	Session		string	`json:"session"`
	Roles		map[string]string	`json:"roles,omitempty"`
	Current		int		`json:"current,omitempty"`
	Public		string	`json:"public,omitempty"`
	Private		map[string]string	`json:"private,omitempty"`
}
