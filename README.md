# The Signing State Machines Blockchain 

## Abstract

In a blockchain, smart-contracts are programs designed to implement a transaction between several parties. Smart-contracts are usually written in programming languages such as Solidity for Ethereum, Golang (Go) or Node.js for Hyperledger fabric based blockchains, or almost about any language according to the underlying blockchain implementation. 

A Signing State Machine (SSM) is a smart contract written with a more constrained paradigm than plain programming languages, based on a finite state automaton.

## Theory of operations

### Agents, roles and identities

Every agent participating in the SSM provides:

  * An identifier
  * A public key

Agents hold the private key corresponding to the public key stored in the SSM, and are never supposed to share it.

In a SSM, transitions are performed by a given role. At execution time, the agent registered for the role performs the transition.

### Initial state

The SSM is initialized with:

  * A mapping of the registered agents on SSM roles.
  * A finite state automaton.
  * Initial data, that can be public like for instance the hash of a document, or private.

### States and Transitions

  * Agents can query the state automaton and the current data.
  * Each state transition is a tuple <role,action>. An agent performs a transition by signing the update with its private key. The SSM validates the transaction by checking the state's signature with the public key of the agent assigned to the corresponding role, and then updates the SSM state.
  * The smart-contract is fulfilled when the SSM enters an acceptance state.

### Data management

  * When performing a transition, an agent updates the target state data. The agent signs the updated state data with its private key. The updated state data must contain the current iteration number that will be incremented by the ssm, preventing unintentional transitions and signature reuse.
  * At any given state, involved agents - i.e assigned to the roles mentioned in a transition originating from therein - can choose to perform a transition according to the current state data.
  * Private data is encrypted using the public keys of the involved agents, that can use it do decide if they perform a transition.
  * The originating agent - who performed the previous transition - can write in the updated state one data entry for each involved agent, encrypted with that agent's public key.
  * Other agents involved can check the multiple encrypted versions of the data and verify that they are identical, by decrypting their own version with their private key, then re-encrypting it with each other agent public key to compare with the corresponding encrypted version in the state data.


## Signing State Machine Chaincode API

  * Command Line Interface [SDK Documentation](sdk/cli/README.md) and [example](sdk/cli/example.md).

### Operation

SSM chaincode API involves Queries and Transations. Queries can be performed without other restictions than those set at chaincode deployment. Transactions on the other hand  have to be signed either by an administrator or a user.

  * Queries follow a "Get by id" model, under the form: `<type>, <identifier>` where the type is one of "ssm", "session", "user", "admin".
  * Transactions have the form: `<arg1>, ...<argn>, <agent>, <signature>` where the signature is a hash of args1..n concatenation signed with the agent's private key.

### Data structures

#### Agent

SSM chaincode agents are identified by a unique name. The agent's public key is the only information stored in the state DB.

```
"Agent": {
	name: "Adam",
	pub: "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3MO+2nIRi/cs4WbE+ykzA1ErTfs0QmBIdpZoAsU7YVMKBnBNulxhy2BI93QHK9uQreLhANBDexagMZg9ZzCxtKLi9UNHSm08099znPfMKn2cITHI8ShyZC7OogsbNmqrY0iy01r4IVpPi4CMNhLTCWyLGWS+L0hsmZOQQWV5BeER4nufBgGmA8plD14T/AXaHF7pMJAGlvauqjcjhb9YAoDUjSmdy4h3KzNq0c1KSQwORgQhgGItUxs5X8jvAXsikRDs7OkqbEDWpSf5z6FSyenvPmnplrqL/5bjiis6ObbOA+BjpMpyuouXOA3WuGv61a5Wrx62bcfeCx9471EKFQIDAQAB"
}
```

There are two kind of agents:

  * **Users** are the agents who can trigger a transition in a SSM. In effect they are the participants in the smart contract represented by the SSM.
  * **Admins** have the rights to add new users and state machines. They are declared only once, when the chaincode is instantiated.

#### SSM state

The SSM state represents a snapshot of a run session on a given state machine. It can be queried and updated from the SSM chaincode API. It holds the current state index, public and private data relevant to the SSM session. The iteration is incremented at every transition. The originating transition allows to track the SSM run session history in the ledger.
The limit is an optional maximum iterations count. Transitions can be performed while the current iteration count is strictly lower than the limit.

```
"State": {
	ssm: "Negociation",
	session: "carsale20190301",
	iteration: 1,
	limit: 10,
	roles: {
		"Bob": "Validator",
		"Sam": "Initiator"
	},
	current: 2,
	origin: {from: 1, to: 2, role: "Validator", action: "Accept"}
	public: "Used car for 100 dollars.",
	private: {
		"Bob": "XXXX",
		"Sam": "YYYY"
	}
}
```

#### SSM transition

The Signing State Machine is created from a list of transitions. A transition is defined by the origin and destination state indexes, an agent name, and an action.

```
"Transition": {
	from: 1,
	to: 2,
	role: "Validator",
	action: "Accept"
}
```

#### Signing State Machine

The SSM structure is just a list of transitions and a unique name.

```
"SigningStateMachine": {
	name: "Negociation",
	transitions: [
		{from: 0, to: 1, role: "Initiator", action: "Propose"},
		{from: 1, to: 2, role: "Validator", action: "Accept"},
		{from: 1, to: 3, role: "Validator", action: "Reject"},
		{from: 1, to: 4, role: "Validator", action: "Amend"},
		{from: 4, to: 1, role: "Initiator", action: "Update"},
		{from: 4, to: 2, role: "Initiator", action: "Accept"},
		{from: 4, to: 3, role: "Initiator", action: "Reject"}
	]
}
```

A new SSM session is intialized with a State structure. Semantics of initial / final states, and reset transitions returning to the initial state are left to the SSM users.

#### Grant

The Grant structure represents a set of admin APIs that are delegated to a given user.
The iteration field is incremented every time an admin updates the user's grant credits.

In the following example, user Bob can create 10 state machines, and start 100 sessions.

```
"Grant": {
	user: "Bob",
	iteration: 0,
	credits: {
		"create": {amount: 10},
		"start": {amount: 100}
	}
}
```

The admin APIs that can be delegated to users are **register**, **create** and **start** i.e. creation APIs only. Other APIs such as "grant" and "limit" tamper with existing users and sessions and cannot be delegated.

### API

#### Chaincode instanciation

  * **init:** creates a new chaincode instance, with a list of admins.

```
Command
-------
  "init", admins: <Agent>*

Result
------
  Chaincode is instantiated, DB stores a list of admins.
```


#### User management

  * **register:** A new user is registered by an admin, Agent structure is signed with the admin private key.

```
Command
-------
  "register", user:Agent, admin_name:string, signature:b64

Result
------
  User is stored in the DB.
```

  * **grant:** A user is granted / revoked API rights by an admin, Grant structure is signed with the admin private key.

```
Command
-------
  "grant", rights:Grant, admin_name:string, signature:b64

Result
------
  Grant is stored / updated in the DB.
```

#### SSM management

  * **create:** A new SSM is registered by an admin, SSM structure is signed with the admin private key.

```
Command
-------
  "create", ssm:SigningStateMachine, admin_name:string, signature:b64

Result
------
  SSM is stored in the DB.
```

  * **start:** A new SSM session is initiated by an admin, SSM init state is signed with the admin private key.

```
Command
-------
  "start", init:State, admin_name:string, signature:b64

Result
------
  State is stored in the DB.
```

  * **limit:** Sets or removes a limit to the maximum iterations count of an existing SSM session. The uppdated session state is signed with the admin private key.

```
Command
-------
  "limit", context:State, admin_name:string, signature:b64

Result
------
  Session state is updated. 
```

**Note**: In the `State` structure of the `context` parameter, `session` and `iteration` fields are mandatory, `limit` field is optional, other fields are read-only.


#### SSM execution

  * **perform:** A user performs an action on a SSM session. The action to perform and the new session state are signed with the user private key.
  
```
Command
-------
  "perform", action:string, context:State, user_name:string, signature:b64

Result
------
  Session state is updated. 
```

**Note**: In the `State` structure of the `context` parameter, `session` and `iteration` fields are mandatory, `public` and `private` data fields are optional, other fields are read-only.

#### SSM queries

  * **session:** Get a SSM session state.

```
Query
-------
  "session", <session id> 

Result
------
  Returns current session State structure.
```

  * **ssm:** Get a SigningStateMachine transitions list.

```
Query
-------
  "ssm", <ssm name> 

Result
------
  Returns corresponding SigningStateMachine structure.
```

  * **user:** Get a user's public key.

```
Query
-------
  "user", <user name> 

Result
------
  Returns corresponding Agent structure.
```

  * **credits:** Get a user's API credits aka grants.

```
Query
-------
  "credits", <user name> 

Result
------
  Returns corresponding Grant structure.
```

  * **admin:** Get an administrator's public key.

```
Query
-------
  "admin", <admin name> 

Result
------
  Returns corresponding Agent structure.
```

  * **list:** List all Sessions, SigningStateMachines, Users or Admins 

```
Query
-------
  "list", <session|ssm|user|admin> 

Result
------
  Returns the list of corresponding identifiers.
```

  * **log:** Log a session history

```
Query
-------
  "log", <session id> 

Result
------
  Returns the successive session states with the corresponding transaction ids.
```
