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

## Docker image
 * Clean docker image
 ```
 make clean-ssm
 ```

 * Build docker image

```
make build-ssm -e VERSION=0.4.2
```

 * Push docker image

```
make push-ssm -e VERSION=0.4.2
```

 * Inspect docker image

```
make inspect-ssm -e VERSION=0.4.2
```

### Package Chaincode and manualy install
```
make clean-ssm package-ssm -e VERSION=0.4.2
```
Package will be created in build/ssm-0.4.2.pak
```
docker cp build/ssm-0.4.2.pak cli-bclan:/opt/gopath/src/github.com/hyperledger/fabric/peer
docker exec -it cli-bclan peer chaincode install ssm-0.4.2.pak
```

## Build java sdk
 * Clean 
 ```
 make clean-ssm-java
 ```

 * Build

```
make build-ssm-java -e VERSION=0.4.2
```

 * Push

```
make push-ssm-java -e VERSION=0.4.2
```