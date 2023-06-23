/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
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

/**
 *
 * @author charalambos
 */
public class VoterVerificationTransaction {
    private String transactionId;
    private PublicKey verifier; //the voter public key
    private PublicKey verifey;
    private Instant timestamp;
    private String city;
    private byte[] signature;


    public VoterVerificationTransaction(PublicKey verifier, PublicKey verifey, String city,Instant timestamp) {
        this.verifier = verifier;
        this.verifey = verifey;
        this.city=city;
        this.timestamp=timestamp;
    }
    public String calculateTransactionHash() {
        String sign_string=Arrays.toString(this.signature);
        String data =createDataString();
        
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
            signatureInstance.initVerify(verifier);
            signatureInstance.update(data.getBytes(StandardCharsets.UTF_8));
            return signatureInstance.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Error verifying transaction signature", e);
        }
    }
    
    public String createDataString(){
        String data=verifier.toString() + verifey.toString() + city + this.timestamp.toString();
        return data;
    }
    

    public boolean isTransactionValid() {
        // Check if the signature is valid
        if (!verifySignature()) {
            return false;
        }
        return true;
    }

    public boolean isVerified(PublicKey voter) {
        
//        System.out.println("this.verifey.equals(voter):"+this.verifey.equals(voter));
//        System.out.println("Verifey: " + this.verifey.toString());
//        System.out.println("Voter: " + voter.toString());
        
        return this.isTransactionValid() && this.verifey.equals(voter); 
    }
    public String getCity(PublicKey voter) {
        
//        System.out.println("this.verifey.equals(voter):"+this.verifey.equals(voter));
//        System.out.println("Verifey: " + this.verifey.toString());
//        System.out.println("Voter: " + voter.toString());
        
        if (this.isVerified(voter)){
            return this.city;
        }
        return "na";
    }

    public String toString(){
        return "VoterVerificationTransaction{" + "verifier=" + this.verifier.toString() + ", verifey=" + this.verifey.toString() + ", city=" + this.city + ", timestamp=" + this.timestamp.toEpochMilli() + '}';

    }

}
