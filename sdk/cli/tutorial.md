# Signing State Machines Command Line Interface Tutorial

  * Enter CLI environment

```
docker exec -it cli-bclocal /bin/bash
source /opt/blockchain-org/session/cli_env
export PATH=/opt/blockchain-org/util:$PATH
```

  * Package and install ssm chaincode

```
peer chaincode package -n ${CHAINCODE} -p blockchain-org/go/${CHAINCODE} -v ${VERSION} ${CHAINCODE}-${VERSION}.pak
peer chaincode install ${CHAINCODE}-${VERSION}.pak
```

  * Deploy ssm chaincode with admin "Adam"

```
# Create keys for "Adam"
rsa_keygen Adam
# Create init.arg string
echo -n '{"Args":["init","[' > init.arg
json_agent Adam Adam.pub | jq . -cM | sed 's/"/\\"/g' | tr -d "\n" >> init.arg
echo -n ']"]}' >> init.arg
# Init chaincode
peer chaincode instantiate -o ${ORDERER_ADDR} --tls --cafile ${ORDERER_CERT} -C ${CHANNEL} -n ${CHAINCODE} -v ${VERSION} -c $(cat init.arg) -P "OR ('BlockchainLocalOrgMSP.member')"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["admin", "Adam"]}'
```

  * Register users "Bob" and "Sam"

```
# Create keys
rsa_keygen Bob
rsa_keygen Sam
# Register users with "Adam" for signer
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(register Bob Adam)"
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(register Sam Adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["user", "Bob"]}'
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["user", "Sam"]}'
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
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(create negociation Adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["ssm", "Negociation"]}'
```

  * Start the "carsale20190301" session

```
echo '{
  "ssm": "Negociation",
  "session": "carsale20190301",
  "roles": {
    "Bob": "Validator",
    "Sam": "Initiator"
  }
}' > carsale20190301.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(start carsale20190301 Adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "carsale20190301"]}'
```

  * Perform transactions 

```
echo '{
  "session": "carsale20190301",
  "public": "1000 dollars 1978 Camaro",
  "iteration": 0
}' > state1.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Propose state1 Sam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "carsale20190301"]}'
```

```
echo '{
  "session": "carsale20190301",
  "public": "800 dollars 1978 Camaro",
  "iteration": 1
}' > state1.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Amend state1 Bob)"
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

  * Adam grants one session creation to Bob

```
echo '{
	"user": "Bob",
	"credits": {
		"start": {"amount": 1}
	}
}
' > grant1.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(grant grant1 Adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["credits", "Bob"]}'
```


  * Backup generated crypto keys and session data

```
cp * /opt/blockchain-org/session
exit
```

