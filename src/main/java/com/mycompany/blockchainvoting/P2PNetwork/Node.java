/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.P2PNetwork;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.mycompany.blockchainvoting.Blockchain.Block;
import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;
/**
 *
 * @author charalambos
 */
public class Node {

    // void registerNode(Node node) throws RemoteException;
    // void addTransaction(VoteTransaction transaction) throws RemoteException;
    // void mineBlock() throws RemoteException;
    // boolean submitMinedBlock(Block block) throws RemoteException;
    // boolean verifyVoter(PublicKey voter) throws RemoteException;
    private NodeSource source;
    private NodeSourceInterface sourceInterface;
    private NodeSink sink;
    private PublicKey id;
    private int source_port;
    private int sink_port;
    private String host="localhost";
    private String name;
    

    public Node(PublicKey id,String host,int source_port,int sink_port)  throws NotBoundException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        this.host=host;
        this.source_port=source_port;
        this.sink_port=sink_port;
        this.id=id;

    }

    public void startSource() throws RemoteException{


        Registry my_Registry=LocateRegistry.createRegistry(this.source_port);
        this.source=new NodeSource();
        this.source.setId(this.id);
        my_Registry.rebind(this.id.toString(), this.source);
    }
    public void setName(String name){
        this.name=name;

    }
    public void startSink() throws NoSuchAlgorithmException, InvalidKeySpecException, RemoteException, IOException{
        File file=new File("wallets.txt");

        FileReader reader=new FileReader(file);

        BufferedReader bufferedReader= new BufferedReader(reader);

        String line;
        while((line=bufferedReader.readLine())!=null){
            byte[] pkbytes=Base64.getDecoder().decode(line);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pkbytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // or other algorithm
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Registry cl_reg=LocateRegistry.getRegistry(host,this.sink_port);
            // if(publicKey.equals(this.id)){
            //     System.out.println("This public key is equal"+publicKey.toString());
            // }

            if(!publicKey.equals(this.id)){
                try{
                    this.sourceInterface=(NodeSourceInterface) cl_reg.lookup(publicKey.toString());
                }catch (Exception e){
                    continue;}
                this.sink=new NodeSink();
                this.sink.setId(publicKey);
                this.sink.setName(this.name);

                this.sourceInterface.register(sink);
            }
        }

    }
    
    public void sendTransaction(VoteTransaction transaction) throws RemoteException{
        this.sourceInterface.sendTransaction(transaction);
    }

    public void sendMessage(String message) throws RemoteException{
        this.sourceInterface.sendMessage(message);
    }
    

    
    
}
