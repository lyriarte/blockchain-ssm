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
	strAgent := "{\"name\": \"John Doe\", \"pub\": \"-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxR0XhIzP0S9WTx7giz0iXqMhuwrjiai7GX8esPKuKMKQuGej5xTpKrfAf6/RtVRNPV3PQy92NqGXk+35nQVnGJU/GEpq86SnRrWWxVSqQR5Nh87DxbR3eoAwcKLFymsixJoWvpm/DU5Ut+Iuqy4Zla2zM5gS62/xlv03VJWVBPFN99pBybPWw0WnRbpnGFIpgDtyMjaE4U48Lmq8wesQ6c2RSXSE/HC76DOhmNKAbgkBnpMxvgW1AGUCJfB4KfutOkLb0OOHIRUeJv+FySwIeXyMh2o3xUQCHWKxSN3Rawg1aJBy2wj1jR9yUAwraLIUzguTaLDUvVH/4eKRGSryzwIDAQAB-----END PUBLIC KEY-----\"}"
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

