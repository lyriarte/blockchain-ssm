# The Signing State Machines Blockchain 

## Abstract

In a blockchain, smart-contracts are programs designed to implement a transaction between several parties. Smart-contracts are usually written in programming languages such as Solidity for Ethereum, Golang (Go) or Node.js for Hyperledger fabric based blockchains, or almost about any language according to the underlying blockchain implementation. 

A Signing State Machine (SSM) is a smart contract written with a more constrained paradigm than plain programming languages, based on a finite state automaton.

## Theory of operations

### Agents and identities

Every agent participating in the SSM provides:

  * An identifier
  * A public key

Agents hold the private key corresponding to the public key stored in the SSM, and are never supposed to share it.

### Initial state

The SSM is initialized with:

  * A set of agents, with their public keys.
  * A finite state automaton.
  * Initial data, that can be public like for instance the hash of a document, or private.

### States and Transitions

  * Agents can query the state automaton and the current data.
  * Each state transition is a tuple <agent,action>. An agent performs a transition by signing the requested action value with its private key. The SSM validates the transaction by checking the action value using the agent's stored public key, and then updates the SSM state.
  * The smart-contract is fulfilled when the SSM enters an acceptance state.

### Data management

  * When performing a transition, an agent can update the target state data.
  * At any given state, involved agents - i.e mentioned in a transition originating from therein - can choose to perform a transition according to the current state data.
  * Private data is encrypted using the public keys of the involved agents, that can use it do decide if they perform a transition.
  * The originating agent - who performed the previous transition - can write one data entry for each involved agent, encrypted with that agent's public key.
  * Other agents involved can check the multiple encrypted versions of the data and verify that they are identical, by decrypting their own version with their public key, then re-encrypting it with each other agent public key to compare with the corresponding encrypted version in the state data.

