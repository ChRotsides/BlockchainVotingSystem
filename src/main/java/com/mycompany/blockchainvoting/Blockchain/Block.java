/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author charalambos
 */
public class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private int nonce;
    private int difficulty;
    private String hash;
    private HashMap<String, VoteEvent> VoteEvents;
    private ArrayList<VoteTransaction> votetransactions;
    private String merkleRoot_vt=null;
    private String merkleRoot_vvt=null;
    private ArrayList<VoterVerificationTransaction> voterverificationtransactions;
    
    public Block(int index, String previousHash,int difficulty){
        
        this.index=index;
        this.previousHash=previousHash;
        this.difficulty=difficulty;
        this.timestamp=new Date().getTime();
        this.votetransactions= new ArrayList<>();
        this.voterverificationtransactions=new ArrayList<>();
        this.VoteEvents=new HashMap<>();
        this.nonce=0;
        }
    
    public String getHash(){
     return this.hash;
    }
    public String calculateHash() {
        
   
        this.merkleRoot_vt=MerkleTree.calculateMerkleRoot_vt(this.votetransactions);
        this.merkleRoot_vvt=MerkleTree.calculateMerkleRoot_vvt(this.voterverificationtransactions);

        String data = this.index + this.timestamp + this.previousHash + this.merkleRoot_vt+ this.merkleRoot_vvt + this.nonce + this.difficulty;
        
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
    public void setNonce(int nonce){
        this.nonce=nonce;
    }
    public boolean addVoterVerificationTransaction(VoterVerificationTransaction vvt){
//        TODO: add return for true or false and add checks if valid or not
        if(vvt.isTransactionValid() && vvt.verifySignature()){
            this.voterverificationtransactions.add(vvt);
            return true;
        }
        
        return false;
    }
    public void addVoteEvent(VoteEvent newevent,Blockchain blockchain){

        for (Block block:blockchain.getChain()){
            for (VoteEvent event:block.getVoteEvents().values()){
                if (event.getEventId().equals(newevent.getEventId())){
                    System.out.println("Event already exists");
                    return;
                }
            }
        }
        if (this.VoteEvents.containsKey(newevent.getEventId())){
            System.out.println("Event already exists");
            return;
        }
        this.VoteEvents.put(newevent.getEventId(),newevent);
    }
        
    private HashMap<String, VoteEvent> getVoteEvents() {
        return this.VoteEvents;
    }

    public VoteEvent getEventFromId(String eventid){
        System.out.println("getEventFromId --> Event id:"+eventid);
        return this.VoteEvents.get(eventid);
    }
    public boolean addVoteTransaction(VoteTransaction vt,Blockchain blockchain){
//        TODO: add return for true or false and add checks if valid or not
//        System.out.println("Is TransactionValid:"+vt.isTransactionValid(blockchain));
//        System.out.println("VerifySignature: "+vt.verifySignature());


        if(vt.isTransactionValid(blockchain,this)&& vt.verifySignature()){
            this.votetransactions.add(vt);
            return true;
        }
        return false;
    }
    public boolean validateTransactions(Blockchain blockchain){
        for (int i=0; i<this.votetransactions.size(); i++){
            if (votetransactions.get(i).isTransactionValid(blockchain,this)!=true){
                return false;
            }
        }
        
    return true;
    }

    public int getVerificationsFor(PublicKey voter){
        int num_verifications=0;
        
//        TODO: Add checks to make sure the verifier only verifies the verifiy once 
//        TODO: Add a hashmap for faster access
//        System.out.println(voterverificationtransactions);
        for (VoterVerificationTransaction vvt : voterverificationtransactions){
            if (vvt.isVerified(voter)){
                num_verifications++;
            }
        }
        
        return num_verifications;
    
    }
        public int getCityVerificationsFor(PublicKey voter,String city){
        int num_verifications=0;
        
//        TODO: Add checks to make sure the verifier only verifies the verifiy once 
//        TODO: Add a hashmap for faster access
//        TODO: Make sure the vvt is valid X
//        System.out.println(voterverificationtransactions);
//        System.out.println("Vvt size: "+this.voterverificationtransactions.size());
        for (VoterVerificationTransaction vvt : this.voterverificationtransactions){
            if(vvt.isTransactionValid()){
                // System.out.println("-----------------------VVT City is valid------------------------");
                if (vvt.getCity(voter).equals(city)){
                    num_verifications++;
                }
            }
        }
        return num_verifications;
    }
        
    public boolean checkIfVoted(PublicKey voter,String eventId){
//        TODO: check if there is a transaction in this block that this voter voted for the event id
        
        for (VoteTransaction vt: votetransactions){
            if (vt.getEventId().equals(eventId) && vt.verifySignature()){
                if (vt.getVoter().equals(voter)){
                    return true;
                }
            }
        }



        return false;
    }
    public HashMap<String,Integer> voteResults(String eventId,VoteEvent event){
//        TODO: Probably needs check for verification
        ArrayList<String> choices=(ArrayList)event.getChoices();
        HashMap<String, Integer> results = new HashMap<>();
        for (String ch: choices){
            results.put(ch,0);
        }
        for (VoteTransaction vt: votetransactions){
            
            if (vt.getEventId().equals(eventId) && vt.verifySignature()){
                int currentValue=results.get(vt.getChoice());
                currentValue+=1;
                results.put(vt.getChoice(),currentValue);
            }
        }
        return results;
    }
    public void print(){
        System.out.println(this.toString());
    }
    public String toString(){

        String str= "Block Index: "+this.index+
                           ", Block timestamp: "+this.timestamp+
                           ", Block previousHash: "+this.previousHash+
                           ", Block Merkle Root for vote transactions: "+this.merkleRoot_vt+
                           ", Block Merkle Root for voter verification transactions: "+this.merkleRoot_vvt+
                           ", Block nonce: "+this.nonce+
                           ", Block difficulty: "+this.difficulty;

        int index=0;
        for (VoteTransaction vt: this.votetransactions){
                           str+=", Block Vote Transaction["+index+"]: "+vt.toString();
                           index++;
                        }
        index=0;
        for (VoterVerificationTransaction vvt: this.voterverificationtransactions){
                           str+=", Block Vote Verification Transaction["+index+"]: "+vvt.toString();
                           index++;
                        }
        index=0;
        for (Map.Entry<String,VoteEvent> set: this.VoteEvents.entrySet()){
                            str+=", Block Vote Event ["+index+"]: "+set.getValue().toString();
                            index++;
                        }

        return str;
    }
    public String toStringLess(){

        String str= "Block Index: "+this.index+
                           ", Block timestamp: "+this.timestamp+
                           ", Block previousHash: "+this.previousHash+
                           ", Block Merkle Root for vote transactions: "+this.merkleRoot_vt+
                           ", Block Merkle Root for voter verification transactions: "+this.merkleRoot_vvt+
                           ", Block nonce: "+this.nonce+
                           ", Block difficulty: "+this.difficulty;

        // int index=0;
        // for (VoteTransaction vt: this.votetransactions){
        //                    str+=", Block Vote Transaction["+index+"]: "+vt.toString();
        //                    index++;
        //                 }
        // index=0;
        // for (VoterVerificationTransaction vvt: this.voterverificationtransactions){
        //                    str+=", Block Vote Verification Transaction["+index+"]: "+vvt.toString();
        //                    index++;
        //                 }
        // index=0;
        // for (Map.Entry<String,VoteEvent> set: this.VoteEvents.entrySet()){
        //                     str+=", Block Vote Event ["+index+"]: "+set.getValue().toString();
        //                     index++;
        //                 }

        return str;
    }

    public ArrayList<VoteTransaction> getVoteTransactions() {
        return this.votetransactions;
    }

    public ArrayList<VoterVerificationTransaction> getVoterVerificationTransactions() {
        return this.voterverificationtransactions;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public int getIndex() {
        return this.index;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public boolean isValid(Blockchain blockchain) {
        String hash = calculateHash();
        
        // Check if the hash meets the difficulty requirement
        String leadingZeros = new String(new char[this.difficulty]).replace('\0', '0');
        if (!hash.substring(0, difficulty).equals(leadingZeros)) {
            return false;
        }
        
        // Check if all vote transactions and voter verification transactions are valid
        for (VoteTransaction vt : votetransactions) {
            if (!vt.isTransactionValid(blockchain, this)) {
                return false;
            }
        }
        for (VoterVerificationTransaction vvt : voterverificationtransactions) {
            if (!vvt.isTransactionValid()) {
                return false;
            }
        }
        return true;
    }

    public void mineBlock() {
        String leadingZeros = new String(new char[this.difficulty]).replace('\0', '0');
        this.hash=calculateHash();
        while (!this.hash.substring(0, difficulty).equals(leadingZeros)) {
            this.nonce++;
            this.hash = calculateHash();
        }
        System.out.println("Block mined: " + this.hash);

    }
}
