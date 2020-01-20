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
	message := "{\"name\":\"Bob\",\"pub\":\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4v2ZYVhQaG7HdhCsrFKlxG1kWLrApCgb2tUjdXLjc4WVG12ZC2eW1b5WZZnsGmS46eCDZyxZsJTxTbKjlKb8FcWWfs0tEixL08gqBKUH9+FD8GaEdtiaOdQJ+yehUBZ5VEty9OrLUMW5L3Ftr9kGmbbtAMRkyRXTT6KEXemm3xRpX/Z2FQzmFmcucbArjyu/LvzlDHJwCiQEQS3R9UCGnjYLZEBYqj7tEEP5cnsm5egwe/EWXx9rvZH9HK3AYVgraE69qZ+FhWkErIqHWmctgPbBYBPXKCfBMzf4INMA4+c5gaCygz4RRFcr10OjGCAS/IiKaLl0X7ehbt6yrYyxEwIDAQAB\"}"
	b64sign := "GotPEetQ34tbVodiGw3prtYWYjf06mMJX5j4X6n8m+KbMYQt0GMRtmVo88jRbXqD71IRlgPfe6dnNZ0dZRIZSUGAk57Y6fuADC0vwDciEg2+IXEc/F4vrMAEh3KXPMwb3MskRA+K1IO91szwcrrn3Y74odrBzmSypTjO60GmPAJybJ9r/5ynafDZFM5wmzgNNecJ51Yz8nltHPhhMOzDIE7pETvrlK58XnarySl81WMgR9GIO64A+WixsEsyURCiYQwWVf9XKAM6a9bDvm8h5fUvfrfktmruz8CB/qJgBLD60WD3qvSCPoN8BgCzI+QgAebyqNejNPbWSP7Z18JpxQ=="
	err = agent.Verify(message, b64sign)
	if err != nil {
		test.Fatal(err)
	}
	
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ")
}


//
// Grant
//

func TestGrant(test *testing.T) {
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Grant")

	var err error
	var grant Grant

	fmt.Println("---- ---- ---- ---- Deserialize")
	strGrant := "{\"user\": \"Bob\",\"iteration\": 0,\"credits\": {\"create\": {\"amount\": 1},\"start\": {\"amount\": 1}}}"
	err = grant.Deserialize([]byte(strGrant))
	if err != nil {
		test.Fatal(err)
	}
	
	fmt.Println("---- ---- ---- ---- Serialize")
	bytesGrant, err := grant.Serialize()
	if err != nil {
		test.Fatal(err)
	}
	if bytesGrant == nil {
		test.Fatal("bytesGrant")
	}
	
	fmt.Println("---- ---- ---- ---- ApiGrant")
	fmt.Println("---- ---- negative")
	err = grant.ApiGrant("Tom","start")
	if err == nil {
		test.Fatal("Should fail due to user.")
	}
	fmt.Println("---- ---- positive")
	err = grant.ApiGrant("Bob","start")
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- negative")
	err = grant.ApiGrant("Bob","start")
	if err == nil {
		test.Fatal("Should fail due to use cont.")
	}
	fmt.Println("---- ---- negative")
	err = grant.ApiGrant("Bob","stop")
	if err == nil {
		test.Fatal("Should fail due to non existing api.")
	}

	fmt.Println("---- ---- ---- ---- SetCredits")
	var update Grant
	strUpdate := "{\"user\": \"Bob\",\"iteration\": 0,\"credits\": {\"start\": {\"amount\": 1}}}"
	err = update.Deserialize([]byte(strUpdate))
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- positive")
	err = grant.SetCredits(&update)
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- negative")
	err = grant.SetCredits(&update)
	if err == nil {
		test.Fatal("Should fail due to iteration.")
	}
	strUpdate = "{\"user\": \"Bob\",\"iteration\": 1,\"credits\": {\"grant\": {\"amount\": 1}}}"
	err = update.Deserialize([]byte(strUpdate))
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- negative")
	err = grant.SetCredits(&update)
	if err == nil {
		test.Fatal("Should fail due to forbidden api.")
	}

	fmt.Println("---- ---- ---- ---- ApiGrant")
	fmt.Println("---- ---- negative")
	err = grant.ApiGrant("Bob","create")
	if err == nil {
		test.Fatal("Should fail due to removed api.")
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
	strState := "{\"ssm\": \"Car dealership\", \"session\": \"deal20181201\", \"public\": \"Used car for 100 dollars.\", \"roles\": {\"Bob\": \"Buyer\", \"Sam\": \"Seller\"}}"
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
	
	fmt.Println("---- ---- ---- ---- Perform")
	var update State
	strUpdate := "{\"session\": \"deal20181201\", \"public\": \"Ok to sell\", \"iteration\": 0, \"private\": {\"Bob\": \"XXX\",\"Sam\": \"YYY\"}}"
	err = update.Deserialize([]byte(strUpdate))
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- positive")
	err = state.Perform(&update, "Role", "Action")
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- negative")
	err = state.Perform(&update, "Role", "Action")
	if err == nil {
		test.Fatal("Should fail due to iteration.")
	}

	fmt.Println("---- ---- ---- ---- Deserialize ---- iteration 0, limit 0")
	strState = "{\"ssm\": \"Car dealership\", \"session\": \"deal20181201\", \"iteration\": 0, \"limit\": 0, \"public\": \"Used car for 100 dollars.\", \"roles\": {\"Bob\": \"Buyer\", \"Sam\": \"Seller\"}}"
	err = state.Deserialize([]byte(strState))
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- Perform ---- negative")
	err = state.Perform(&update, "Role", "Action")
	if err == nil {
		test.Fatal("Should fail due to limit.")
	}
	fmt.Println("---- ---- Limit ---- 1")
	*state.Limit = 1
	fmt.Println("---- ---- Perform ---- positive")
	err = state.Perform(&update, "Role", "Action")
	if err != nil {
		test.Fatal(err)
	}
	
	fmt.Println("---- ---- ---- ---- Deserialize ---- string payload")
	strState = "{\"ssm\": \"Loop\", \"session\": \"looptest\", \"iteration\": 0, \"public\": \"Zero.\", \"roles\": {\"Luke\": \"Looper\"}}"
	err = state.Deserialize([]byte(strState))
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- ---- ---- Deserialize ---- float payload")
	strState = "{\"ssm\": \"Loop\", \"session\": \"looptest\", \"iteration\": 0, \"public\": 3.14159, \"roles\": {\"Luke\": \"Looper\"}}"
	err = state.Deserialize([]byte(strState))
	if err != nil {
		test.Fatal(err)
	}
	fmt.Println("---- ---- ---- ---- Deserialize ---- hash payload")
	strState = "{\"ssm\": \"Loop\", \"session\": \"looptest\", \"iteration\": 0, \"public\": {\"astring\": \"Hello\", \"anint\":1234}, \"roles\": {\"Luke\": \"Looper\"}}"
	err = state.Deserialize([]byte(strState))
	if err != nil {
		test.Fatal(err)
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
	
	fmt.Println("---- ---- ---- ---- NextState")
	var stt int
	fmt.Println("---- ---- positive")
	stt = ssm.NextState(1, "Buyer", "Buy")
	if stt != 2 {
		test.Fatal("Failed accepting transition")
	}
	fmt.Println("---- ---- negative")
	stt = ssm.NextState(0, "Buyer", "Buy")
	if stt != -1 {
		test.Fatal("Failed rejecting transition")
	}
	
	fmt.Println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ")
}

