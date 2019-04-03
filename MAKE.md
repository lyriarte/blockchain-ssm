# Make

## Package ssm in docker image

The image civisblockchain/ssm a simple docker image based on linux alpine.
It contains:
```
/opt/civis-blockchain/ssm/chaincode/go/ssm/  --> Sources of the chaincode
/opt/civis-blockchain/ssm/util               --> Bash script to instanciate, invoke and query the chaincode
/opt/civis-blockchain/ssm/env                --> Env properties: CHAINCODE=ssm VERSION=0.4.2
/opt/civis-blockchain/ssm/ssm-0.4.2.pak      --> Packaged chaincode
```

`/opt/civis-blockchain/ssm/env` contains:
```
CHAINCODE=ssm
VERSION=0.4.2
```

## Release process
*  Define version
```
VERSION=0.4.2
```

*  Prepare git tag
```
git tag -a ${VERSION} -m "${VERSION} version"
git checkout ${VERSION}
```

* Build, tag as latest version and push docker images
```
make build tag-latest push -e VERSION=${VERSION}
```

* Push git tag
```
git push origin ${VERSION}
```

## Build docker image

```
make build -e VERSION=0.4.2
```

### Push docker image

```
make push -e VERSION=0.4.2
```

### Inspect docker image

```
make inspect -e VERSION=0.4.2
```

### Package Chaincode and manualy install
```
make clean package-ssm -e VERSION=0.4.2
```
Package will be created in build/ssm-0.4.2.pak
```
docker cp build/ssm-0.4.2.pak cli-bclan:/opt/gopath/src/github.com/hyperledger/fabric/peer
docker exec -it cli-bclan peer chaincode install ssm-0.4.2.pak
```