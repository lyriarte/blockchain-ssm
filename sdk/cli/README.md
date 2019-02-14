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

## Command line APIs Usage

The CLI APIs generate the JSON args for the peer chaincode commands.

  * Register a User

```
  "register", user:Agent, admin_name:string, signature:b64

Usage: register <user> <signer>
```

  * Create a Signing State Machine

```
  "create", ssm:SigningStateMachine, admin_name:string, signature:b64

Usage: create <ssm> <signer>
```

  * Start a Session

```
  "start", init:State, admin_name:string, signature:b64

Usage: start <session> <signer>
```

  * Perform an action

```
  "perform", action:string, context:State, user_name:string, signature:b64

Usage: perform <action> <context> <signer>
```
