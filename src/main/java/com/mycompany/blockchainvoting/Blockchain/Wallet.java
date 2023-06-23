package com.mycompany.blockchainvoting.Blockchain;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.time.Instant;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author charalambos
 */
public class Wallet {
    private PrivateKey privatekey;
    private PublicKey publickey;
    private keyPairHolder keypairholder;
    /**
     *
     * @param privateKey
     * @param publicKey
     */
    public Wallet(PrivateKey privateKey,PublicKey publicKey){
        this.privatekey=privateKey;
        this.publickey=publicKey;
        
    
    }
    public Wallet(){
        try {
            // Create a KeyPairGenerator instance for RSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            // Initialize the key pair generator with a key size and a secure random instance
            SecureRandom secureRandom = new SecureRandom();
            keyPairGenerator.initialize(2048, secureRandom);

            // Generate the key pair
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get the public and private keys
            this.publickey = keyPair.getPublic();
            this.privatekey = keyPair.getPrivate();

            
            this.keypairholder=new keyPairHolder(this.publickey,this.privatekey);
            
            
            // Print the keys in Base64 encoded format
            // System.out.println("Public Key: " + java.util.Base64.getEncoder().encodeToString(this.publickey.getEncoded()));
            // System.out.println("Private Key: " + java.util.Base64.getEncoder().encodeToString(this.privatekey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error generating RSA key pair: " + e.getMessage());
        }
        
    
    }
    
    public keyPairHolder getKeypair(){
        
        if(keypairholder!=null){
            return this.keypairholder;
        }
        return null;
    }
    public VoterVerificationTransaction verifyAWallet(PublicKey publictoVerify,String city){
        
        Instant timestamp=Instant.now();
        VoterVerificationTransaction vvt=new VoterVerificationTransaction(this.publickey,publictoVerify,city,timestamp);
        
        vvt.signTransaction(this.privatekey);
        // System.out.println(vvt);
        return vvt;
        
        
    }
    
    public VoteTransaction vote(String voteChoice,String voteEventId){
        Instant timestamp=Instant.now();
        VoteTransaction vt=new VoteTransaction(this.publickey,voteEventId,voteChoice,timestamp);
        vt.signTransaction(this.privatekey);
        return vt;
    }
    public VoteEvent voteEvent(String voteCity,String voteEventId,long eventDutation){
        // Instant timestamp=Instant.now();
        VoteEvent ve=new VoteEvent(this.publickey,voteCity,voteEventId,eventDutation);
        ve.signTransaction(this.privatekey);
        return ve;
    }
    
    
}
