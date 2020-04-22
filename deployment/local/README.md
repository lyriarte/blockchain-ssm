# Network configuration: bclocal


## Initial configuration


### Cryptography

```
cryptogen generate --config=./crypto-config.yaml
```

### Artefacts

```
export FABRIC_CFG_PATH=$PWD
```

#### Orderers

  * Orderer genesis block

```
configtxgen -profile BlockchainLocalOrdererGenesis -channelID testchainid -outputBlock ./config/genesis.block
```

#### Channels

```
export CHANNEL=sandbox
```

  * Channel configuration transaction 

```
configtxgen -profile BlockchainLocalOrgChannels -outputCreateChannelTx ./config/${CHANNEL}.tx -channelID $CHANNEL
```

  * Anchor peer transaction

```
configtxgen -profile BlockchainLocalOrgChannels -outputAnchorPeersUpdate ./config/BlockchainLocalOrgMSPanchors.tx -channelID $CHANNEL -asOrg BlockchainLocalOrg
```

## Network setup

### Docker containers environment

```
export ORGA=bc-org.bclocal

echo COMPOSE_PROJECT_NAME="bclocal" > .env
echo ca__CA_KEYFILE=$(basename $(ls crypto-config/peerOrganizations/${ORGA}/ca/*_sk)) >> .env
echo ca__TLS_KEYFILE=$(basename $(ls crypto-config/peerOrganizations/${ORGA}/tlsca/*_sk)) >> .env
echo ca__ADMIN=$(cat /dev/urandom | xxd | head -n 1 | cut -b 10-49 | sed "s/ //g") >> .env
echo ca__PASSWD=$(cat /dev/urandom | xxd | head -n 1 | cut -b 10-49 | sed "s/ //g") >> .env

echo cli_ORGA=${ORGA} >> .env
echo cli_USER=Admin >> .env
```

### Start network

```
docker-compose -f docker-compose.yaml up -d ca.bc-org.bclocal orderer.bclocal peer0.bc-org.bclocal cli.bc-org.bclocal
```

### Use the CLI container environment


  * Runtime CLI configuration

```
cli_ORGA=bc-org.bclocal
echo cli_ORGA=${cli_ORGA} > session/cli_env

# orderer address and certificate
echo ORDERER_ADDR="orderer.bclocal:7050" >> session/cli_env
echo ORDERER_CERT="/etc/hyperledger/orderer/tlsca.bclocal-cert.pem" >> session/cli_env

# current session chaincode
echo CHANNEL="sandbox" >> session/cli_env
echo CHAINCODE="ssm" >> session/cli_env
echo VERSION="0.8.2" >> session/cli_env
```

```
# Enter CLI environment
docker exec -it cli-bclocal /bin/bash
```

```
# Channel creation in CLI environment
source /opt/blockchain-org/session/cli_env
peer channel create -o ${ORDERER_ADDR} -c ${CHANNEL} -f /etc/hyperledger/config/${CHANNEL}.tx --tls --cafile ${ORDERER_CERT}
peer channel join -b ${CHANNEL}.block
```

```
exit
```

### Stop network and cleanup

```
docker-compose -f docker-compose.yaml down
```

```
git clean -fdx
```

