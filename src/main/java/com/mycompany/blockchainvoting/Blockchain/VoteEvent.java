/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charalambos
 */
public class VoteEvent {
    
    private String voteEventId;
    private PublicKey initializerKey;
    private ArrayList<String> choices;
    // start time of the VoteEvent
    private long startTime;
    // end time of the VoteEvent
    private long endTime;
    private boolean signed=false;
    private byte[] signature;
    private String voteCity;
    public VoteEvent(PublicKey initializerKey,String voteCity,String voteEventId,long eventDutation){
        this.voteEventId=voteEventId;
        this.initializerKey=initializerKey;
        this.voteCity=voteCity;
        this.choices=new ArrayList<>();
        this.startTime=System.currentTimeMillis();
        this.endTime=startTime+eventDutation;
    }
    
    public boolean addOption(String option){
        if (this.signed==false){
            this.choices.add(option);

            return true;
        }
        return false;
    }
    public String getEventId(){
        return this.voteEventId;
    }
    public String getVoteCity(){
        return this.voteCity;
    }
    public ArrayList<String> getChoices(){
        return this.choices;
    }
    public void signTransaction(PrivateKey privateKey) {
        try {
            String data = this.createDataString();
            Signature signatureInstance = Signature.getInstance("SHA256withRSA");
            signatureInstance.initSign(privateKey);
            signatureInstance.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = signatureInstance.sign();
            this.signature = signatureBytes;
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
            signatureInstance.initVerify(initializerKey);
            signatureInstance.update(data.getBytes(StandardCharsets.UTF_8));
            return signatureInstance.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Error verifying transaction signature", e);
        }
    }
    

    public boolean isEventActive(){
        long currentTime=System.currentTimeMillis();
        if (currentTime>=this.startTime && currentTime<=this.endTime){
            return true;
        }
        return false;
    }

    public String createDataString(){
        String accu="";
        for (String ch: choices){
            accu+=ch;
        }
        accu+=this.voteEventId+this.initializerKey+this.voteCity+this.startTime+this.endTime;
        return accu;
    }
    
    public String toString(){

        return "VoteEventId: "+this.voteEventId+" VoteCity: "+this.voteCity+" Choices: "+this.choices.toString();
    }
    
    
}
