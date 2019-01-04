// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"errors"

	"crypto"
	"crypto/x509"
	"encoding/json"
	"encoding/pem"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type Agent struct {
	AgentModel
}

//
// Storable interface implementation
//

func (self *Agent) Put(stub shim.ChaincodeStubInterface, key string) error {
	data, err := self.Serialize()
	if (err != nil) {
		return err
	}	
	return stub.PutState(key, data)
}

func (self *Agent) Get(stub shim.ChaincodeStubInterface, key string) error {
	data, err := stub.GetState(key);
	if (err != nil) {
		return err
	}	
	return self.Deserialize(data)
}

//
// Serializable interface implementation
//

func (self *Agent) Serialize() ([]byte, error) {
	return json.Marshal(self.AgentModel)
}

func (self *Agent) Deserialize(data []byte) error {
	return json.Unmarshal(data, &self.AgentModel)
}

//
// Agent API implementation
//

func (self *Agent) PublicKey() (crypto.PublicKey, error) {
	// PEM from base 64
	pemStr := "-----BEGIN PUBLIC KEY-----\n"
	i := 0;
	for i < len(self.AgentModel.Pub)-64 {
		pemStr += self.AgentModel.Pub[i:i+64] + "\n"
		i += 64
	}
	pemStr += self.AgentModel.Pub[i:len(self.AgentModel.Pub)] + "\n"
	pemStr += "-----END PUBLIC KEY-----\n"
	
	// Public Key from PEM
	block, _ := pem.Decode([]byte(pemStr))
	if block == nil {
		return nil, errors.New("Invalid agent PEM block")
	}
	if block.Type != "PUBLIC KEY" {
		return nil, errors.New("Agent PEM block is not a public key")
	}

	return x509.ParsePKIXPublicKey(block.Bytes)
}
