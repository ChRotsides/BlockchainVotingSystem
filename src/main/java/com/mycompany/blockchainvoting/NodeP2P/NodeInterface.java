package com.mycompany.blockchainvoting.NodeP2P;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import com.mycompany.blockchainvoting.Blockchain.Block;
import com.mycompany.blockchainvoting.Blockchain.Blockchain;
import com.mycompany.blockchainvoting.Blockchain.VoteEvent;
import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;
import com.mycompany.blockchainvoting.Blockchain.VoterVerificationTransaction;

public interface NodeInterface extends Remote {
    void addNodesink(NodeInterface node) throws RemoteException;
    void addNodesource(NodeInterface node) throws RemoteException;
    
    public Blockchain sendBlockChain() throws RemoteException;
    public void getBlockChainFromNetwork() throws RemoteException, InterruptedException;

    void send(String message) throws RemoteException;
    void receive(String message,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException;
    public void receiveVt(VoteTransaction vt,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException, InterruptedException, IOException;
    public void sendVt(VoteTransaction vt) throws RemoteException, InterruptedException, IOException;
    public void receiveVvt(VoterVerificationTransaction vvt,Set<NodeInterface> visitedNodes,ArrayList<NodeInterface> willBeVisited ) throws RemoteException, InterruptedException;
    public void sendVvt(VoterVerificationTransaction vvt) throws RemoteException, InterruptedException;
    public void receiveBlock(Block block,Set<NodeInterface> visitedNodes ,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException;
    public void sendBlock(Block block) throws RemoteException, InterruptedException;
    public void createBlock() throws RemoteException, InterruptedException;
    public void sendChain(Blockchain chain) throws RemoteException, InterruptedException;
    public void sendChain() throws RemoteException, InterruptedException;
    public void receiveChain(Blockchain chain,Set<NodeInterface> visitedNodes ,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException;
    public void sendVE(VoteEvent ve) throws RemoteException, InterruptedException;
    public void receiveVE(VoteEvent ve,Set<NodeInterface> visitedNodes ,ArrayList<NodeInterface> willBeVisited) throws RemoteException, InterruptedException;
}
