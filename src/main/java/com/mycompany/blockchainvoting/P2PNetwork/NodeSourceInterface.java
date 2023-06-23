package com.mycompany.blockchainvoting.P2PNetwork;

import java.rmi.*;
import java.security.PublicKey;

import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;
public interface NodeSourceInterface  extends Remote{
    public void setId(PublicKey id) throws RemoteException;
    public PublicKey getId() throws RemoteException;
    public void sendTransaction(VoteTransaction transaction) throws RemoteException;
    public void register(NodeSinkInterface sink) throws RemoteException;
    public String getName() throws RemoteException;
    public void sendMessage(String message) throws RemoteException;

}
