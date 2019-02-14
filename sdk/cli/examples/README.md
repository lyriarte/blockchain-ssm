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
VERSION=0.1.0
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

  * Register users "bob" and "bam"

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

  * Create the "Car dealership" ssm

```
echo '{
  "name": "Car dealership",
  "transitions": [
    {"from": 0, "to": 1, "role": "Seller", "action": "Sell"},
    {"from": 1, "to": 2, "role": "Buyer", "action": "Buy"}
  ]
}' > car_dealership.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(create car_dealership adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["ssm", "Car dealership"]}'
```

  * Start the "deal20181201" session

```
echo '{
  "ssm": "Car dealership",
  "session": "deal20181201",
  "public": "Used car for 100 dollars.",
  "roles": {
    "bob": "Buyer",
    "sam": "Seller"
  }
}' > deal20181201.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(start deal20181201 adam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "deal20181201"]}'
```

  * Perform transactions 

```
echo '{
  "session": "deal20181201",
  "public": "100 dollars 1978 Camaro",
  "iteration": 0
}' > state1.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Sell state1 sam)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "deal20181201"]}'
```

```
echo '{
  "session": "deal20181201",
  "public": "Deal !",
  "iteration": 1
}' > state2.json
peer chaincode invoke -o ${ORDERER_ADDR} -C ${CHANNEL} -n ${CHAINCODE} --tls --cafile ${ORDERER_CERT} -c "$(perform Buy state2 bob)"
```

```
peer chaincode query -C ${CHANNEL} -n ${CHAINCODE} -c '{"Args":["session", "deal20181201"]}'
```


  * Backup generated crypto keys and session data

```
cp * /opt/blockchain-coop
```

