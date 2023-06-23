package com.mycompany.blockchainvoting.P2PNetwork;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;

import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;

public class NodeSink extends UnicastRemoteObject implements NodeSinkInterface {

    private PublicKey id;
    private String name="";

    protected NodeSink() throws RemoteException{
        super();
    }
    // public NodeSink(PublicKey id) throws RemoteException{

    //     super();
    //     this.setId(id);
    // }

    public void sendMessage(String message) throws RemoteException{
        System.out.println(this.name+" says: Received message: "+ message);
    }

    public void setId(PublicKey id) throws RemoteException{
        this.id=id;
    }
    public PublicKey getId() throws RemoteException{
        return id;

    }
    public void sendTransaction(VoteTransaction transaction) throws RemoteException{
        System.out.println("Received transaction...: "+ transaction.toString());
    }
    public void setName(String string) throws RemoteException {
        this.name=string;
    }
    public String getName() throws RemoteException{
        return this.name;
    }

}
