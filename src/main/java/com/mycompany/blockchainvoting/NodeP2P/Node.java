package com.mycompany.blockchainvoting.NodeP2P;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.mycompany.blockchainvoting.Blockchain.Block;
import com.mycompany.blockchainvoting.Blockchain.Blockchain;
import com.mycompany.blockchainvoting.Blockchain.VoteEvent;
import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;
import com.mycompany.blockchainvoting.Blockchain.VoterVerificationTransaction;

public class Node extends UnicastRemoteObject implements NodeInterface {
    private String name;
    public ArrayList<NodeInterface> nodesink;
    public ArrayList<NodeInterface> nodesource;
    private PublicKey id;
    private boolean visited = false;
    public boolean isMiner = false;
    private Set<NodeInterface> visitedNodes;
    private Semaphore semaphore;
    Blockchain blockChain;
    ArrayList<VoteTransaction> pending_vt_pool;
    ArrayList<VoterVerificationTransaction> pending_vvt_pool;
    ArrayList<VoteEvent> pending_ve_pool;
    


    public Node(String name,PublicKey id,Semaphore sem,boolean isMiner) throws RemoteException {
        super();
        this.name=name;
        this.id=id;
        this.nodesink = new ArrayList<NodeInterface>();
        this.nodesource = new ArrayList<NodeInterface>();
        this.visitedNodes = new HashSet<NodeInterface>();
        this.semaphore=sem;
        this.blockChain = new Blockchain();
        this.pending_ve_pool = new ArrayList<VoteEvent>();
        this.pending_vt_pool = new ArrayList<VoteTransaction>();
        this.pending_vvt_pool = new ArrayList<VoterVerificationTransaction>();
        this.isMiner=isMiner;
    }

    public void getBlockChainFromNetwork() throws RemoteException, InterruptedException {
        this.semaphore.acquire();
        for (NodeInterface node : nodesink) {
            // System.out.println(this.blockChain.toString());
            node.sendChain();

        }
        this.semaphore.release();
    }

    public void sendChain() throws RemoteException, InterruptedException {
        this.semaphore.acquire();
        if (this.blockChain != null) {
            // System.out.println(this.name + " sending message: " + this.blockChain.toString());
            propagateChain(this.blockChain,visitedNodes,new ArrayList<NodeInterface>());
        }else{
            System.out.println(this.name + " sending message: " + "null");
        }
        this.semaphore.release();

    }

    public void receiveChain(Blockchain chain,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException, InterruptedException {
        this.semaphore.acquire();
        // System.out.println(this.name + " received message: " + chain.toString());
        if (chain!=null) {
            if (chain.getSize() > this.blockChain.getSize() && chain.isValid()) {
                this.blockChain = chain;
            }
        }
        this.semaphore.release();
        propagateChain(chain,visitedNodes,willBeVisited);
    }
    
    private void propagateChain(Blockchain chain,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException {
        // Mark this node as visited to avoid loops
        visitedNodes.add(this);

        // Forward the message to unvisited nodes
        for (NodeInterface node : nodesink) {
            if (!visitedNodes.contains(node) && !willBeVisited.contains(node)) {
                node.receiveChain(chain,visitedNodes,this.nodesink);
            }
        }

        // Remove this node from the visited nodes list
        visitedNodes.remove(this);
    }

    public Blockchain sendBlockChain() throws RemoteException {
        return this.blockChain;
    }

    public void addNodesink(NodeInterface node) throws RemoteException {
        this.nodesink.add(node);
    }

    public void addNodesource(NodeInterface node) throws RemoteException {
        this.nodesource.add(node);
    }

    public void send(String message) throws RemoteException {
        // System.out.println(this.name + " sending message: " + message);
        propagate(message,visitedNodes,new ArrayList<NodeInterface>());
    }

    public void receive(String message,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException {
        // System.out.println(this.name + " received message: " + message);
    
        // Propagate the message to all nodes in the network
        propagate(message,visitedNodes,willBeVisited);
    }
    
    private void propagate(String message,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited) throws RemoteException {
        // Mark this node as visited to avoid loops
        visitedNodes.add(this);

        // Forward the message to unvisited nodes
        for (NodeInterface node : nodesink) {
            if (!visitedNodes.contains(node) && !willBeVisited.contains(node)) { 
                node.receive(message,visitedNodes,this.nodesink);
            }
        }

        // Remove this node from the visited nodes list
        visitedNodes.remove(this);
    }


    public void sendVt(VoteTransaction vt) throws InterruptedException, IOException {
        this.semaphore.acquire();
        // System.out.println(this.name + " sending message: " + vt.toString());
        this.semaphore.release();
        propagateVt(vt,visitedNodes,new ArrayList<NodeInterface>());
    }

    public void receiveVt(VoteTransaction vt,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws InterruptedException, IOException {
        System.out.println(this.name + " received message: " + vt.toString());
        // Propagate the message to all nodes in the network
        this.semaphore.acquire();
        if (this.pending_vt_pool.contains(vt)) {
            System.out.println("Vote Transaction already in the pool");
        }else{
            // Block block = new Block(blockChain.getLastBlock().getIndex()+1,blockChain.getLastBlock().getHash(),blockChain.getLastBlock().getDifficulty());
            // for (VoterVerificationTransaction vvt : this.pending_vvt_pool) {
            //     block.addVoterVerificationTransaction(vvt);
            // }
            // for (VoteTransaction vtt : this.pending_vt_pool) {
            //     block.addVoteTransaction(vtt, blockChain);
            // }
            // vt.isTransactionValid(this.blockChain,block);
            this.pending_vt_pool.add(vt);
            if (this.isMiner && this.pending_vt_pool.size()>=3) {
                FileWriter writer = new FileWriter(this.name+"block.txt");
                System.out.println(this.name + " creating block with pending Vote transactions");
                String aString="Block:"+this.getChain().getLastBlock().getIndex()+1+"\nVts:\n";
                for (VoteTransaction vt1 : this.pending_vt_pool) {
                    aString+=vt1.toString();
                }
                writer.write(aString);
                writer.close();
                this.createBlock();
            }

        }
        this.semaphore.release();
        propagateVt(vt,visitedNodes,willBeVisited);
    }
    
    private void propagateVt(VoteTransaction vt,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited) throws InterruptedException, IOException {
        // Mark this node as visited to avoid loops
        visitedNodes.add(this);

        // Forward the message to unvisited nodes
        for (NodeInterface node : nodesink) {
            if (!visitedNodes.contains(node)&& !willBeVisited.contains(node)) {
                node.receiveVt(vt,visitedNodes,this.nodesink);
            }
        }

        // Remove this node from the visited nodes list
        visitedNodes.remove(this);
    }


    public void sendVvt(VoterVerificationTransaction vvt) throws RemoteException, InterruptedException {
        // System.out.println(this.name + " sending message: " + vvt.toString());
        // this.pending_vvt_pool.add(vvt);
        propagateVvt(vvt,visitedNodes,new ArrayList<NodeInterface>());
    }

    public void receiveVvt(VoterVerificationTransaction vvt,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException, InterruptedException {
        // System.out.println(this.name + " received message: " + vvt.toString());
        // Propagate the message to all nodes in the network
        this.semaphore.acquire();
        if (this.pending_vvt_pool.contains(vvt)) {
            System.out.println("Transaction already in the pool");
        }else{
            this.pending_vvt_pool.add(vvt);
            if (this.isMiner && this.pending_vvt_pool.size()>=3) {
                System.out.println(this.name+" Creating block");
                this.createBlock();
            }
        }
        this.semaphore.release();
        propagateVvt(vvt,visitedNodes,willBeVisited);
    }
    
    private void propagateVvt(VoterVerificationTransaction vvt,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException {
        // Mark this node as visited to avoid loops
        visitedNodes.add(this);

        // Forward the message to unvisited nodes
        for (NodeInterface node : nodesink) {
            if (!visitedNodes.contains(node)&& !willBeVisited.contains(node)) {
                node.receiveVvt(vvt,visitedNodes,this.nodesink);
            }
        }

        // Remove this node from the visited nodes list
        visitedNodes.remove(this);
    }

    public void sendBlock(Block block) throws RemoteException, InterruptedException {
        System.out.println(this.name + " sending Block: " + block.toStringLess());
        propagateBlock(block,visitedNodes,new ArrayList<NodeInterface>());
    }

    public void receiveBlock(Block block,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException, InterruptedException {
        System.out.println(this.name + " received Block: " + block.toStringLess());
        // Propagate the message to all nodes in the network
        this.semaphore.acquire();
        if (this.blockChain.contains(block)) {
            System.out.println("Block already in the blockChain");
        }else{
           if( block.isValid(this.blockChain) && this.blockChain.getLastBlock().getIndex()>block.getIndex()){
                this.blockChain.addBlock(block);
                for (VoterVerificationTransaction vvt : block.getVoterVerificationTransactions()) {
                    if(this.pending_vvt_pool.contains(vvt)){
                        this.pending_vvt_pool.remove(vvt);
                    }
                }
                for (VoteTransaction vt : block.getVoteTransactions()) {
                    if(this.pending_vt_pool.contains(vt)){
                        this.pending_vt_pool.remove(vt);
                    }
                }

           }
            
        }
        this.semaphore.release();
        propagateBlock(block,visitedNodes,willBeVisited);
    }
    
    private void propagateBlock(Block block,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException {
        // Mark this node as visited to avoid loops
        visitedNodes.add(this);

        // Forward the message to unvisited nodes
        for (NodeInterface node : nodesink) {
            if (!visitedNodes.contains(node)&& !willBeVisited.contains(node)) {
                node.receiveBlock(block,visitedNodes,this.nodesink);
            }
        }

        // Remove this node from the visited nodes list
        visitedNodes.remove(this);
    }


    public void createBlock() throws RemoteException, InterruptedException{
        Block block = new Block(blockChain.getLastBlock().getIndex()+1,blockChain.getLastBlock().getHash(),blockChain.getLastBlock().getDifficulty());
        for (VoterVerificationTransaction vvt : this.pending_vvt_pool) {
            block.addVoterVerificationTransaction(vvt);
        }
        for (VoteTransaction vtt : this.pending_vt_pool) {
            block.addVoteTransaction(vtt, blockChain);
        }

        for (VoteEvent ve : this.pending_ve_pool) {
            block.addVoteEvent(ve,this.blockChain);
        }
        this.pending_vt_pool.clear();
        this.pending_vvt_pool.clear();
        this.pending_ve_pool.clear();
        block.mineBlock();
        blockChain.addBlock(block);
        this.sendBlock(block);

    }

    public String getName() {
        return this.name;
    }

    public boolean setBlockchain(Blockchain chain) {
        if(chain != null && chain.isValid()){
            this.blockChain = chain;
            return true;
        }
        return false;
    }

    public Blockchain getBlockChain() {
        return this.blockChain;
    }

    public void sendChain(Blockchain chain) throws RemoteException, InterruptedException {
        // System.out.println(this.name + " sending message: " + chain.toString());
        propagateChain(chain,visitedNodes,new ArrayList<NodeInterface>());
    }

    public void sendVE(VoteEvent ve) throws RemoteException, InterruptedException {
        System.out.println(this.name + " sending message: " + ve.toString());
        this.pending_ve_pool.add(ve);
        propagateVE(ve,visitedNodes,new ArrayList<NodeInterface>());
    }

    public void receiveVE(VoteEvent ve,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException, InterruptedException {
        System.out.println(this.name + " received message: " + ve.toString());
        // Propagate the message to all nodes in the network
        this.semaphore.acquire();
        if (this.pending_ve_pool.contains(ve)) {
            System.out.println("Transaction already in the pool");
        }else{
            this.pending_ve_pool.add(ve);
            if (this.isMiner && this.pending_ve_pool.size()>=3) {
                this.createBlock();
            }
        }
        this.semaphore.release();
        propagateVE(ve,visitedNodes,willBeVisited);
    }
    
    private void propagateVE(VoteEvent ve,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException {
        // Mark this node as visited to avoid loops
        visitedNodes.add(this);

        // Forward the message to unvisited nodes
        for (NodeInterface node : nodesink) {
            if (!visitedNodes.contains(node)&& !willBeVisited.contains(node)) {
                node.receiveVE(ve,visitedNodes,this.nodesink);
            }
        }

        // Remove this node from the visited nodes list
        visitedNodes.remove(this);
    }

    public Blockchain getChain() {
        return this.blockChain;
    }





}
