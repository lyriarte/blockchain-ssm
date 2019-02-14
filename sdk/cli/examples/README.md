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

