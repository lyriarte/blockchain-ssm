# Java Sdk for blockchain-ssm

[Blockchain-ssm](https://github.com/civis-blockchain/blockchain-ssm) is a signing state machines chaincode developped for Hyperleger Fabric.  
This sdk has been tested with [fabric bclan configuration](https://github.com/civis-blockchain/blockchain-coop/tree/master/bclan) fabric network configuration

## Configuration exemple

```
ssm.rest.url=http://peer0.pr-bc1.civis-blockchain.org:9090
```

Ex: `ssm-rest-client/src/test/resources/ssm-client.properties`

##Usage example
``` 
sdk-core/src/test/java/io/civis/ssm/sdk/client/SsmClientItTest.java
```