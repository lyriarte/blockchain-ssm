// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

import (
	"errors"

	"crypto"
	"crypto/x509"
	"crypto/sha256"
	"crypto/rsa"
	"encoding/json"
	"encoding/pem"
	"encoding/base64" 

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
	var i int
	for i = 0; i < len(self.AgentModel.Pub)-64; i += 64 {
		pemStr += self.AgentModel.Pub[i:i+64] + "\n"
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

func (self *Agent) Verify(message string, b64sign string) error {
	// Retrieve agent's public key
	pubKey, err := self.PublicKey()
	if err != nil {
		return err
	}
	// Explicit cast to RSA public key
	pubKeyRSA := pubKey.(*rsa.PublicKey)
	// Decode base 64 signature
	signature, err := base64.StdEncoding.DecodeString(b64sign)
	if err != nil {
		return err
	}
	// Calculate message hash
	hash := sha256.Sum256([]byte(message))
	// Verify signature
	return rsa.VerifyPKCS1v15(pubKeyRSA, crypto.SHA256, hash[:], signature)
}
