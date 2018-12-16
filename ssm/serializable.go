// Copyright Luc Yriarte <luc.yriarte@thingagora.org> 2018 
// License: Apache-2.0

package main

type Serializable interface {
	Serialize() ([]byte, error)
	Deserialize(data []byte) error
}
