# Example session

  * Using the bclan network settings

```
# Use blockchain-ssm CLI SDK in blockchain-coop CLI environment
cp sdk/cli/util/* ../blockchain-coop/util/
# Enter CLI environment
docker exec -it cli-bclan /bin/bash
export PATH=/opt/blockchain-coop/:$PATH
```

```
cli_ORGA=bc-coop.bclan
ORDERER_ADDR=orderer.bclan:7050
ORDERER_CERT=/etc/hyperledger/orderer/tlsca.bclan-cert.pem
CHANNEL=sandbox
CHAINCODE=ssm
VERSION=0.4.0
```

  * Install ssm chaincode

```
peer chaincode install -n ${CHAINCODE} -v ${VERSION} -p blockchain-coop/go/ssm/
```

  * Deploy ssm chaincode with admin "adam"

```
# Create keys for "adam"
rsa_keygen adam
# Create init.arg string
echo -n '{"Args":["init","[' > init.arg
json_agent adam adam.pub | jq . -cM | sed 's/"/\\"/g' | tr -d "\n" >> init.arg
echo -n ']"]}' >> init.arg
# Init chaincode
peer chaincode instantiate -o ${ORDERER_ADDR} --tls --cafile ${ORDERER_CERT} -C ${CHANNEL} -n ${CHAINCODE} -v ${VERSION} -c $(cat init.arg) -P "OR ('BlockchainLANCoopMSP.member')"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["admin", "adam"]}'
```

  * Register users "bob" and "sam"

```
# Create keys
rsa_keygen bob
rsa_keygen sam
# Register users with "adam" for signer
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(register bob adam)"
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(register sam adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["user", "bob"]}'
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["user", "sam"]}'
```

  * Create the "Negociation" ssm

```
echo '{
	"name": "Negociation",
	"transitions": [
		{"from": 0, "to": 1, "role": "Initiator", "action": "Propose"},
		{"from": 1, "to": 2, "role": "Validator", "action": "Accept"},
		{"from": 1, "to": 3, "role": "Validator", "action": "Reject"},
		{"from": 1, "to": 4, "role": "Validator", "action": "Amend"},
		{"from": 4, "to": 1, "role": "Initiator", "action": "Update"},
		{"from": 4, "to": 2, "role": "Initiator", "action": "Accept"},
		{"from": 4, "to": 3, "role": "Initiator", "action": "Reject"}
	]
}' > negociation.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(create negociation adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["ssm", "Negociation"]}'
```

  * Start the "carsale20190301" session

```
echo '{
  "ssm": "Negociation",
  "session": "carsale20190301",
  "public": "Used car for 100 dollars.",
  "roles": {
    "bob": "Validator",
    "sam": "Initiator"
  }
}' > carsale20190301.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(start carsale20190301 adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "carsale20190301"]}'
```

  * Perform transactions 

```
echo '{
  "session": "carsale20190301",
  "public": "100 dollars 1978 Camaro",
  "iteration": 0
}' > state1.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Propose state1 sam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "carsale20190301"]}'
```

```
echo '{
  "session": "carsale20190301",
  "iteration": 5
}' > loop.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Amend loop bob)"
echo '{
  "session": "carsale20190301",
  "iteration": 6
}' > loop.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Update loop sam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "carsale20190301"]}'
```

  * List DB state contents

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["list", "admin"]}'
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["list", "user"]}'
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["list", "ssm"]}'
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["list", "session"]}'
```

  * Log session history

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["log", "carsale20190301"]}'
```

  * Backup generated crypto keys and session data

```
cp * /opt/blockchain-coop
exit
```

