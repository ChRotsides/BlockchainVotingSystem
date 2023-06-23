package com.mycompany.blockchainvoting.P2PNetwork;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;

import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;

public class NodeSource extends UnicastRemoteObject implements NodeSourceInterface{



    private PublicKey id;
    private NodeSinkInterface sinks[];
    private String name;

    protected NodeSource() throws RemoteException {
        super();
        sinks=new NodeSinkInterface[10];
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public void setId(PublicKey id) throws RemoteException {
       this.id=id;
       this.name=this.id.toString();
    }

    @Override
    public PublicKey getId() throws RemoteException {
        return this.id;
    }
    public String getName() throws RemoteException{
        return this.name;
    }

    @Override
    public void sendTransaction(VoteTransaction transaction) throws RemoteException {
        for (int i=0; i<sinks.length; i++){
            if (sinks[i]!=null){
                sinks[i].sendTransaction(transaction);
            }
        }
    }
    public void sendMessage(String message) throws RemoteException{
        for (int i=0; i<sinks.length; i++){
            if (sinks[i]!=null){
                sinks[i].sendMessage(message);
            }
        }
    }

    @Override
    public void register(NodeSinkInterface sink) throws RemoteException {
        for (int i=0; i<sinks.length; i++){
            if (sinks[i]==null){
                sinks[i]=sink;
                return;
            }
        }
    }


    
    
}
