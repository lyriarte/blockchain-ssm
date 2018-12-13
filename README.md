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

  * A mapping of the SSM roles on registered agents.
  * A finite state automaton.
  * Initial data, that can be public like for instance the hash of a document, or private.

### States and Transitions

  * Agents can query the state automaton and the current data.
  * Each state transition is a tuple <role,action>. An agent performs a transition by signing the requested action with its private key. The SSM validates the transaction by checking the action signature with the public key of the agent assigned to the corresponding role, and then updates the SSM state.
  * The smart-contract is fulfilled when the SSM enters an acceptance state.

### Data management

  * When performing a transition, an agent can update the target state data.
  * At any given state, involved agents - i.e assigned to the roles mentioned in a transition originating from therein - can choose to perform a transition according to the current state data.
  * Private data is encrypted using the public keys of the involved agents, that can use it do decide if they perform a transition.
  * The originating agent - who performed the previous transition - can write one data entry for each involved agent, encrypted with that agent's public key.
  * Other agents involved can check the multiple encrypted versions of the data and verify that they are identical, by decrypting their own version with their private key, then re-encrypting it with each other agent public key to compare with the corresponding encrypted version in the state data.


## Signing State Machine Chaincode API

### Data structures

#### Agent

SSM chaincode agents are identified by a unique name. The agent's public key is the only information stored in the state DB.

```
"Agent": {
	name: "John Doe",
	pub: "AAAAB3NzaC1yc2EAAAADAQA"
}
```

There are two kind of agents:

  * **Users** are the agents who can trigger a transition in a SSM. In effect they are the participants in the smart contract represented by the SSM.
  * **Admins** have the rights to add new users and state machines. They are declared only once, when the chaincode is instanciated.

#### SSM state

The SSM state represents a snapshot of a run session on a given state machine. It can be queried and updated from the SSM chaicode API. It holds the current state index, public and private data relevant to the SSM session.

```
"State": {
	ssm: "Car dealership",
	session: "deal20181201",
	current: 0,
	roles: {
		"Buyer": "John Doe",
		"Seller": "Joe Black"
	}
	public: "Some non encrypted data",
	private: {
		"John Doe": "FDST54EGFH5bdfe66654",
		"Joe Black": "33STXXFH5bdfe3334566"
	}
}
```

#### SSM transition

The Signing State Machine is created from a list of transitions. A transition is defined by the origin and destination state indexes, an agent name, and an action.

```
"Transition": {
	from: 1,
	to: 2,
	role: "Buyer",
	action: "Buy"
}
```

#### Signing State Machine

The SSM structure is just a list of transitions and a unique name.

```
"SigningStateMachine": {
	name: "Car dealership",
	transitions: [{from: 0,	to: 1, role: "Seller", action: "Sell"},{from: 1, to: 2, role: "Buyer", action: "Buy"}]
}
```

A new SSM session is intialized with a State structure. Semantics of initial / final states, and reset transitions returning to the initial state are left to the SSM users.

### API

#### Chaincode instanciation

  * **init:** creates a new chaincode instance, with a list of admins.

```
Command
-------
  "init", admins: <Agent>*

Result
------
  Chaincode is instanciated, DB stores a list of admins.
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

#### SSM execution

  * **perform:** A user performs an action on a SSM session. The new session state is signed with the user private key.
  
```
Command
-------
  "perform", action:string, context:State, user_name:string, signature:b64

Result
------
  Session state is updated. 
```

**Note**: In the `State` structure of the `context` parameter, `session` field is mandatory, `public` and `private` data fields are optional, other fields are read-only.

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

  * **admin:** Get an administrator's public key.

```
Query
-------
  "admin", <admin name> 

Result
------
  Returns corresponding Agent structure.
```


