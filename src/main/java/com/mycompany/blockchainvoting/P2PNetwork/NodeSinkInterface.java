package com.mycompany.blockchainvoting.P2PNetwork;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

import com.mycompany.blockchainvoting.Blockchain.Block;
import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;

public interface NodeSinkInterface extends Remote{
    
    public void setId(PublicKey id) throws RemoteException;
    public PublicKey getId() throws RemoteException;
    public void setName(String string) throws RemoteException;
    // public void sendMessage(String message,Node node) throws RemoteException;
    public String getName()throws RemoteException;
    public void sendTransaction(VoteTransaction transaction) throws RemoteException;
    public void sendMessage(String message) throws RemoteException;

}
