# SSM Command Line Interface

## Setup

The SSM command-line interface client works with the hyperledger peer CLI. The tools and api scripts also need `jq` and `openssl` installed.

```
sudo apt-get install -y jq openssl
```

Add the api and tools folder to the path before using the CLI tools.

```
export PATH=$(pwd)/api:$(pwd)/tools:"$PATH"
```
