

## Build Chaincode Package
```
make clean package-ssm -e VERSION=0.4.1
```

Package will be created in build/ssm-0.4.1.pak

## Install Chaincode Package

```
docker cp build/ssm-0.4.1.pak cli-bclan:/opt/gopath/src/github.com/hyperledger/fabric/peer
docker exec -it cli-bclan peer chaincode install ssm-0.4.1.pak
```