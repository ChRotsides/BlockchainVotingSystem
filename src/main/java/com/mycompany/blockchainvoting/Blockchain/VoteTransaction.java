/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
/**
 *
 * @author charalambos
 */
public class VoteTransaction {
    private String transactionId;
    private PublicKey voter; //the voter public key
    private String votingEventId;
    private String votingChoice;
    private byte[] signature;
    private Instant timestamp;


    public VoteTransaction(PublicKey voter, String votingEventId, String votingChoice,Instant timestamp) {
        this.voter = voter;
        this.votingEventId = votingEventId;
        this.votingChoice = votingChoice;
        this.timestamp=timestamp;
    }
    public String calculateTransactionHash() {
//        String hash="EmptyHash";
//      TODO: Implementation of the cryptographic hash function (e.g., SHA-256) A method to compute the hash of the transaction by combining its components and applying a cryptographic hash function (e.g., SHA-256).
        String sign_string=Arrays.toString(this.signature);
        String data = this.transactionId + this.voter.toString() + this.votingEventId + this.votingChoice + sign_string + this.timestamp;
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }



//        return hash;
    }
    public String getEventId(){
        return this.votingEventId;
    }
    public String getChoice(){
        return this.votingChoice;
    }
    public byte[] getBytes(){
//        TODO: Find a way to get the whole structare as bytes
//        return this.votingEventId.getBytes()  this.signature;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeUTF(this.transactionId);
            dos.writeLong(this.voter.hashCode());
            dos.writeUTF(this.votingChoice);
            dos.writeUTF(this.votingEventId);
            dos.write(this.signature);
            int sign_size=0;
            for (Byte bt: this.signature){
                sign_size++;
            
            }
            
            dos.writeInt(sign_size);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing block data", e);
        }
//
        return baos.toByteArray();




//        return this.signature;
    }

    public void signTransaction(PrivateKey privateKey) {
        try {
            String data = this.createDataString();
            Signature signatureInstance = Signature.getInstance("SHA256withRSA");
            signatureInstance.initSign(privateKey);
            signatureInstance.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = signatureInstance.sign();
            this.signature = signatureBytes;
            // System.out.print("Sign: "+Arrays.toString(this.signature));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Error signing transaction", e);
        }
    }

    public boolean verifySignature() {
        if (signature == null) {
            return false;
        }

        try {
            String data = this.createDataString();
            Signature signatureInstance = Signature.getInstance("SHA256withRSA");
            signatureInstance.initVerify(voter);
            signatureInstance.update(data.getBytes(StandardCharsets.UTF_8));
            return signatureInstance.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Error verifying transaction signature", e);
        }
    }
    
    public String createDataString(){
        String data=voter.toString() + this.votingEventId + this.votingChoice+this.timestamp.toString();
        return data;
    
    
    }
    public PublicKey getVoter(){
        return this.voter;
    }
    

    public boolean isTransactionValid(Blockchain blockchain,Block block) {
        // Check if the signature is valid
        if (!verifySignature()) {
            System.out.println("Vote transaction Failed: Signature Verification Failed");
            return false;
        }

        // Check if the voter has already voted
        if (blockchain.hasVoted(voter,votingEventId,block)) {

            System.out.println("Vote transaction Failed: Voter has already voted");
            return false;
        }

        
        // Check if the voter is known by at least 3 people
        if (!blockchain.isKnownByAtLeastThree(voter,block)) {
            System.out.println("Vote transaction Failed: Voter has not been verified");
            return false;
        }

        // Check if the voter is voting in their city
        if (!blockchain.isInSameCity(voter, votingEventId,block)) {
            System.out.println("Vote transaction Failed: Voter is not in the same city");
            return false;
        }



        return true;
    }
    public String toString(){

        return "Voter: "+this.voter.toString()+"\n"+"Event: "+this.votingEventId+"\n"+"Choice: "+this.votingChoice+"\n"+"Signature: "+new String(Arrays.toString(this.signature))+"\n"+"Timestamp: "+this.timestamp.toString();

    }
}
