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
	strAgent :=  "{\"name\":\"Adam\",\"pub\":\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3MO+2nIRi/cs4WbE+ykzA1ErTfs0QmBIdpZoAsU7YVMKBnBNulxhy2BI93QHK9uQreLhANBDexagMZg9ZzCxtKLi9UNHSm08099znPfMKn2cITHI8ShyZC7OogsbNmqrY0iy01r4IVpPi4CMNhLTCWyLGWS+L0hsmZOQQWV5BeER4nufBgGmA8plD14T/AXaHF7pMJAGlvauqjcjhb9YAoDUjSmdy4h3KzNq0c1KSQwORgQhgGItUxs5X8jvAXsikRDs7OkqbEDWpSf5z6FSyenvPmnplrqL/5bjiis6ObbOA+BjpMpyuouXOA3WuGv61a5Wrx62bcfeCx9471EKFQIDAQAB\"}"
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
	
	fmt.Println("---- ---- ---- ---- Verify")
	message := "Adam the admin, Sam the seller and Bob the buyer."
	b64sign := "UqEaW5pfefoaJXFbN7so6jziSwqLCZftytzHanFv1OgeLX/Dvl5PQCPPyUpR3dubCqbtOKSQiJdcMkBsC56V2ZGzzO7c0/phQKp2NGD0IlgsEDYwbR3ok9Ah52ZC9ZAeaWEALUJ0mZ8N58u8VbVV1zR9YPJNTO9LzDEjM95lOgMMtEWq+O++qM+F/kZQjPNHZUYk2gdANnQWfSeB73O2oG+WTHJTNKar0k9JTkklhOMVMGDhKfrADyTeYonrlS6vNlbLQSOB2rH1f/QruzxBMzNj0z7HO+7dY0qf/PpwtcVTKOz+5y9HYeyTFP0ZABT0lSRaDx9X0aNeBzodYItm6w=="
	err = agent.Verify(message, b64sign)
	if err != nil {
		test.Fatal(err)
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

