// ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0
//
// Signing State Machines tests
//
// ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----

package main

import (
	"testing"
	"fmt"
)


//
// Agent
//

func TestAgent(test *testing.T) {
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Agent")

	var err error
	var agent Agent

	fmt.Println("---- ---- ---- ---- Deserialize")
	strAgent := "{\"name\": \"John Doe\", \"pub\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxR0XhIzP0S9WTx7giz0iXqMhuwrjiai7GX8esPKuKMKQuGej5xTpKrfAf6/RtVRNPV3PQy92NqGXk+35nQVnGJU/GEpq86SnRrWWxVSqQR5Nh87DxbR3eoAwcKLFymsixJoWvpm/DU5Ut+Iuqy4Zla2zM5gS62/xlv03VJWVBPFN99pBybPWw0WnRbpnGFIpgDtyMjaE4U48Lmq8wesQ6c2RSXSE/HC76DOhmNKAbgkBnpMxvgW1AGUCJfB4KfutOkLb0OOHIRUeJv+FySwIeXyMh2o3xUQCHWKxSN3Rawg1aJBy2wj1jR9yUAwraLIUzguTaLDUvVH/4eKRGSryzwIDAQAB\"}"
	err = agent.Deserialize([]byte(strAgent))
	if err != nil {
		test.Fatal(err)
	}
	
	fmt.Println("---- ---- ---- ---- Serialize")
	bytesAgent, err := agent.Serialize()
	if err != nil {
		test.Fatal(err)
	}
	if bytesAgent == nil {
		test.Fatal("bytesAgent")
	}
	
	fmt.Println("---- ---- ---- ---- PublicKey")
	pubKey, err := agent.PublicKey()
	if err != nil {
		test.Fatal(err)
	}
	if pubKey == nil {
		test.Fatal("pubKey")
	}
	
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ")
}


//
// State
//

func TestState(test *testing.T) {
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- State")

	var err error
	var state State

	fmt.Println("---- ---- ---- ---- Deserialize")
	strState := "{\"ssm\": \"Car dealership\", \"session\": \"deal20181201\", \"current\": 0, \"public\": \"Car dealer 2018 public\", \"private\": {\"John Doe\": \"XXX\",\"Joe Black\": \"YYY\"}, \"roles\": {\"Buyer\": \"John Doe\", \"Seller\": \"Joe Black\"}}"
	err = state.Deserialize([]byte(strState))
	if err != nil {
		test.Fatal(err)
	}
	
	fmt.Println("---- ---- ---- ---- Serialize")
	bytesState, err := state.Serialize()
	if err != nil {
		test.Fatal(err)
	}
	if bytesState == nil {
		test.Fatal("bytesState")
	}
	
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ")
}


//
// SigningStateMachine
//

func TestSigningStateMachine(test *testing.T) {
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- SigningStateMachine")

	var err error
	var ssm SigningStateMachine

	fmt.Println("---- ---- ---- ---- Deserialize")
	strSigningStateMachine := "{\"name\": \"Car dealership\", \"transitions\": [{\"from\": 0, \"to\": 1, \"role\": \"Seller\", \"action\": \"Sell\"}, {\"from\": 1, \"to\": 2, \"role\": \"Buyer\", \"action\": \"Buy\"}]}"
	err = ssm.Deserialize([]byte(strSigningStateMachine))
	if err != nil {
		test.Fatal(err)
	}
	
	fmt.Println("---- ---- ---- ---- Serialize")
	bytesSigningStateMachine, err := ssm.Serialize()
	if err != nil {
		test.Fatal(err)
	}
	if bytesSigningStateMachine == nil {
		test.Fatal("bytesSigningStateMachine")
	}
	
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ")
}

