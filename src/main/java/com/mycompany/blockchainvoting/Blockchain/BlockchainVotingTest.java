/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.blockchainvoting.Blockchain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author charalambos
 */
public class BlockchainVotingTest {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Blockchain chain;
        Block block = new Block(0,"previousHas",3);

        // block.print();
        
        Wallet wallet1=new Wallet();
        Wallet wallet2=new Wallet();
        Wallet wallet3=new Wallet();
        Wallet wallet4=new Wallet();
        
        VoterVerificationTransaction vvt1=wallet1.verifyAWallet(wallet4.getKeypair().getPublicKey(),"Nicosia");
        VoterVerificationTransaction vvt2=wallet2.verifyAWallet(wallet4.getKeypair().getPublicKey(),"Nicosia");
        VoterVerificationTransaction vvt3=wallet3.verifyAWallet(wallet4.getKeypair().getPublicKey(),"Nicosia");
        
        boolean ver1=block.addVoterVerificationTransaction(vvt1);
        boolean ver2=block.addVoterVerificationTransaction(vvt2);
        boolean ver3=block.addVoterVerificationTransaction(vvt3);
        
//        System.out.println("Verifications: "+ver1+" "+ver2+" "+ver3);
//         VoteEvent(PublicKey initializerKey,String voteCity,String voteEventId)
        VoteEvent newevent=new VoteEvent(wallet2.getKeypair().getPublicKey(),"Nicosia","Event1",1000000);
        
        newevent.addOption("null1");
        newevent.addOption("null2");
        newevent.addOption("null3");
        newevent.addOption("null4");
        
        System.out.println(newevent.getChoices());
        System.out.println(newevent.toString());
        chain=new Blockchain(block);
        block.addVoteEvent(newevent,chain);
        block.mineBlock();
        chain.addBlock(block);
        // System.out.println(block.toString());
        // chain.addVoteEvent(newevent);
        VoteTransaction vt1=wallet4.vote("null1", "Event1");
        VoteTransaction vt2=wallet3.vote("null1", "Event1");
        VoteTransaction vt3=wallet4.vote("null1", "Event1");
        
        Block block2=new Block(1,block.calculateHash(),3);
        
        System.out.println("TransactionHash: "+vt2.calculateTransactionHash());
        System.out.println("Block hash: "+ block.calculateHash());
        boolean accepted1=block2.addVoteTransaction(vt1, chain);
        boolean accepted2=block2.addVoteTransaction(vt2, chain);
        boolean accepted3=block2.addVoteTransaction(vt3, chain);
        
        block2.mineBlock();
        chain.addBlock(block2);
        System.out.println("----------------------------------------------------------");
        System.out.println(chain.isValid());
        System.out.println("----------------------------------------------------------");
        // System.out.println("Results: "+chain.getEventResults("Event1").get("null1")+" "+
        //                     +chain.getEventResults("Event1").get("null2")+" "+
        //                     chain.getEventResults("Event1").get("null3")+" "+
        //                     chain.getEventResults("Event1").get("null4"));
        // System.out.println("----------------------------------------------------------");
        // block.print();
        // System.out.println("----------------------------------------------------------");
        if (accepted1 && accepted2 && accepted3){
            System.out.println("All 3 Accepted");
        }else if (accepted1 && accepted2){
            System.out.println("Accepted 1 and 2");
        }else if(accepted1){
            System.out.println("Accepted 1 only");
        }else if (accepted2){
            System.out.println("Accepted 2 only");
        }else if (accepted3){
            System.out.println("Accepted 3 only");
        }else{
            System.out.println("None accepted");
            
        }

        
        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);
        map1.put("C", 3);

        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("A", 1);
        map2.put("B", 2);
        map2.put("C", 3);
        
        map1.replaceAll((key, value) -> value + map2.get(key));
        System.out.println(map1);
        
//      if miner

//      if node

//      if user
        
        
        
    }
}
