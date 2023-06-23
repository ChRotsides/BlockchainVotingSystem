# Blockchain Voting System 

This repository contains a Java implementation of a blockchain-based voting system. Each node in the system can connect to any other node and broadcast new transactions, blocks, or vote events to the entire network.

## Classes
The system is composed of several key classes:

1. Block
The Block class represents a block in the blockchain. It contains a list of vote transactions, voter verification transactions, and vote events, which represent the actions that occur during a voting event. The Block class also maintains necessary metadata to ensure the integrity of the blockchain.

2. Blockchain
The Blockchain class represents the blockchain structure. It contains an array list of Block objects, including a special genesis block that represents the start of the blockchain.

3. VoteEvent
The VoteEvent class represents a voting event within the voting system. It contains information about the voting event such as choices, duration, and a unique identifier for each voting event.

4. VoterVerificationTransaction
The VoterVerificationTransaction class represents a voter verification transaction within the blockchain voting system. Each instance of the VoterVerificationTransaction class represents a verification action, verifying the identity of a voter in a specific city.

5. VoteTransaction
The VoteTransaction class represents a vote transaction within the blockchain voting system. Each instance of the VoteTransaction class represents a vote action, recording a voter's choice in a specific voting event.

6. Wallet
The Wallet class represents a wallet within the blockchain voting system. It manages the cryptographic keys for a user, verifies other users, and creates vote transactions and voting events.

7. Node
The Node class represents a node within the blockchain voting system. It manages communication between nodes in the network, sends and receives various types of messages, and collects transactions to create new blocks in the blockchain.

## How to Use
This implementation is designed to support a voting system with event creation, voter verification, and voting transactions. To use the system, start by creating nodes and linking them together. Then, create wallets for the voters and use them to create voting events, verify voters, and cast votes.

Remember to always validate the blockchain after adding new blocks to ensure its integrity. Also, always make sure that the mining process is done correctly by adjusting the difficulty level accordingly.

For more detailed usage instructions, refer to the documentation of each class.

## Contribute
Contributions to the blockchain voting system are welcomed. If you have a bug or an idea, feel free to open an issue or a pull request.

License
This blockchain voting system is open source and is licensed under the MIT License. See the LICENSE file for more details.
