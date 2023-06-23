/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author charalambos
 */
public class Blockchain {
    
    
    private ArrayList<Block> blocks;
    private Block genesisBlock;
    public Blockchain(Block genBlock){
        this.genesisBlock=genBlock;
        this.blocks=new ArrayList<>();
    }
    public Blockchain(){
        this.genesisBlock=new Block(0,"0",3);
        this.blocks=new ArrayList<>();
    }
//    
//            // Check if the voter has already voted
//        if (blockchain.hasVoted(voter,votingEventId)) {
//            return false;
//        }
//
//        // Check if the voter is voting in their city
//        if (!blockchain.isInSameCity(voter, votingEventId)) {
//            return false;
//        }
//
//        // Check if the voter is known by at least 3 people
//        if (!blockchain.isKnownByAtLeastThree(voter)) {
//            return false;
//        }
    
    public boolean hasVoted(PublicKey voter, String votingEventId,Block block){
//        Search the blockchain and if there is transaction that this voter has voted for the votingEvent id return false
        ArrayList<Block> blocks_copy=(ArrayList<Block>) this.blocks.clone();
        blocks_copy.add(block);
        for (Block b : blocks_copy){
          if(b.checkIfVoted(voter,votingEventId)==true){
              return true;
          }
        }
        return false;
    }

    public boolean isValid() {
        // Check that the genesis block is valid
        if (!genesisBlock.calculateHash().equals(genesisBlock.getHash())) {
            System.out.println("Invalid genesis block hash.");
            return false;
        }
        
        // Check that each block is linked to the previous one and has a valid hash
        for (int i = 1; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i-1);
            
            // Check that the current block is linked to the previous one
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Block " + i + " is not linked to the previous block.");
                return false;
            }
            
            // Check that the current block has a valid hash
            if (!currentBlock.calculateHash().equals(currentBlock.getHash())) {
                System.out.println("Block " + i + " has an invalid hash.");
                return false;
            }
        }
        
        // // Check that all transactions in the blockchain are valid
        // for (Block block : blocks) {
        //     for (VoteTransaction transaction : block.getVoteTransactions()) {
        //         if (!transaction.isTransactionValid(this, block)) {
        //             System.out.println("Invalid transaction: " + transaction.toString());
        //             return false;
        //         }
        //     }
        //     for (VoterVerificationTransaction transaction : block.getVoterVerificationTransactions()) {
        //         if (!transaction.isTransactionValid()) {
        //             System.out.println("Invalid voter verification transaction: " + transaction.toString());
        //             return false;
        //         }
        //     }
        // }
        
        return true;
    }

    public int getSize(){
        return this.blocks.size();
    }


    public boolean isInSameCity(PublicKey voter, String votingEventId,Block block){
//        check that the voter city and the votingEvent are in the same city
        int city_verification = 0;
        ArrayList<Block> blocks_copy=(ArrayList<Block>) this.blocks.clone();
        blocks_copy.add(block);
        VoteEvent event=null;
        for (Block b:blocks_copy){
            event=b.getEventFromId(votingEventId);
            if (event!=null){
                break;
            }
        }
        if (event==null){
            System.out.println("Event not found with id: "+votingEventId);
            return false;
        }
        System.out.println(event.toString());
        String city=event.getVoteCity();
        // System.out.println("Event City:"+city);
        for (Block b : blocks_copy){
          city_verification+=b.getCityVerificationsFor(voter,city);
        }
        System.out.println("CityVerNm:"+city_verification);
        return city_verification>=3;
    }
    
    public boolean isKnownByAtLeastThree(PublicKey voter,Block block){
//    check that this user is verified by at least 3 people
    ArrayList<Block> blocks_copy=(ArrayList<Block>) this.blocks.clone();
    blocks_copy.add(block);
    int n_verifications=0;
    // System.out.print("Blocks: "+this.blocks);
     for (Block b : blocks_copy){
          n_verifications+=b.getVerificationsFor(voter);
     }
//     System.out.println(n_verifications);
     return n_verifications>=3;
    }
    
    public int howmanyknow(PublicKey voter){
        //    check that this user is verified by at least 3 people
            int n_verifications=0;
            // System.out.print("Blocks: "+this.blocks);
             for (Block b : blocks){
                  n_verifications+=b.getVerificationsFor(voter);
             }
        //     System.out.println(n_verifications);
             return n_verifications;
        }
            

    public boolean addBlock(Block newblock){
        return this.blocks.add(newblock);
    }
    public Block getBlock(int index){
        return this.blocks.get(index);
    }
    public Block getLastBlock(){
        return this.blocks.get(this.blocks.size()-1);
    }

    public HashMap<String, Integer> getEventResults(String eventId) {
        VoteEvent event = null;
        
        // Find the event by its ID
        for (Block b : blocks) {
            event = b.getEventFromId(eventId);
            if (event != null) {
                break;
            }
        }
        
        // If the event is not found, print a message and return null
        if (event == null) {
            System.out.println("Event not found with id: " + eventId);
            return null;
        }
        
        // Initialize the results hashmap with the event choices and set their counts to 0
        ArrayList<String> choices = event.getChoices();
        HashMap<String, Integer> results = new HashMap<>();
        for (String choice : choices) {
            results.put(choice, 0);
        }
        
        // Iterate through the blocks and update the vote counts for each choice
        for (Block b : blocks) {
            HashMap<String, Integer> blockResults = b.voteResults(eventId, event);
            // System.out.println("Block results: " + blockResults);
            for (String choice : choices) {
                results.put(choice, results.get(choice) + blockResults.get(choice));
            }
        }
        
        return results;
    }

    public boolean contains(Block block) {
        for (Block b : blocks) {
            if (b.getHash().equals(block.getHash())) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Block> getChain() {
        return this.blocks;
    }
    public Block getGenesisBlock() {
        return this.genesisBlock;
    }
    
    public String toString(){
        String s="";
        for (Block b : this.blocks){
            s+=b.toStringLess()+"\n";
        }
        return s;
    }
}
