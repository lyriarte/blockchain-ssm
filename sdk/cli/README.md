# SSM Command Line Interface

## Setup

The SSM command-line interface client works with the hyperledger peer CLI. The utility scripts also need `jq` and `openssl` installed.

```
sudo apt-get install -y jq openssl
```

Add the util folder to the path before using the CLI SDK.

```
export PATH=sdk/cli/util:"$PATH"
```
