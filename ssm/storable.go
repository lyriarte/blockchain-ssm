// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type Storable interface {
	Put(stub shim.ChaincodeStubInterface, key string) error
	Get(stub shim.ChaincodeStubInterface, key string) error
}
