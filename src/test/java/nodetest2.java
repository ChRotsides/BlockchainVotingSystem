import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.Semaphore;

import com.mycompany.blockchainvoting.Blockchain.Block;
import com.mycompany.blockchainvoting.Blockchain.Blockchain;
import com.mycompany.blockchainvoting.Blockchain.VoteEvent;
import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;
import com.mycompany.blockchainvoting.Blockchain.VoterVerificationTransaction;
import com.mycompany.blockchainvoting.Blockchain.Wallet;
import com.mycompany.blockchainvoting.NodeP2P.Node;

public class nodetest2 {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello nodetest!");
        ArrayList<Wallet> wallets=new ArrayList<>();
        ArrayList<Node> nodes=new ArrayList<>();
        Semaphore sem=new Semaphore(3);
        
        for (int i = 0; i < 7; i++) {
            Wallet cur_wallet=new Wallet();
            wallets.add(cur_wallet);
            nodes.add(new Node("node"+i, cur_wallet.getKeypair().getPublicKey(),sem, i%2==0));
        }


        // File file=new File("wallets");

        // FileWriter writer=new FileWriter("wallets.txt");
        // String publicKeyString1 = Base64.getEncoder().encodeToString(wallet.getKeypair().getPublicKey().getEncoded());
        // String publicKeyString2 = Base64.getEncoder().encodeToString(wallet2.getKeypair().getPublicKey().getEncoded());
        // String publicKeyString3 = Base64.getEncoder().encodeToString(wallet3.getKeypair().getPublicKey().getEncoded());

        // writer.write(publicKeyString1+"\n"+publicKeyString2+"\n"+publicKeyString3);
        // writer.close();




        // // Node
        // Node node1=new Node("node1",wallet.getKeypair().getPublicKey(),true);
        // Node node2=new Node( "node2",wallet2.getKeypair().getPublicKey(),true);
        // Node node3=new Node("node3",wallet3.getKeypair().getPublicKey(),false);

        Registry registry1 = LocateRegistry.createRegistry(1090);

        for (Node node : nodes) {
            registry1.rebind(node.getName(), node);
        }

        for (Node node : nodes) {
            for (Node node2 : nodes) {
                if(node!=node2){
                    node.addNodesink(node2);
                }
            }
        }

        // for (int i=0;i<nodes.size()-1;i++) {

        //     nodes.get(i).addNodesink(nodes.get(i+1));
        //     System.out.println("Node "+i+" added Node "+(i+1));
        // }


        // registry1.rebind("node1", node1);
        // registry1.rebind("node2", node2);
        // registry1.rebind("node3", node3);

        // node1.addNodesink(node2);
        // node1.addNodesink(node3);
        // // node1.addNodesink(node2);
        // node2.addNodesink(node3);
        // node3.addNodesink(node1);
        // node3.addNodesink(node2);


        // node1.send("Node1 says hello");
        // node2.send("Node2 says hello");
        // ArrayList<Thread> threads=new ArrayList<>();
        // for (Node node : nodes) {
        //     Thread thread = new Thread(() -> {
        //         try {
        //             node.send("hello from "+node.getName());
        //         } catch (RemoteException e) {
        //             // TODO Auto-generated catch block
        //             e.printStackTrace();
        //         }
        //     });
        //     threads.add(thread);
        // }

        // for (Thread thread : threads) {
        //     thread.start();
        // }

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
        block.addVoteEvent(newevent, chain);
        block.mineBlock();
        chain.addBlock(block);
        // System.out.println(block.toString());
        // chain.addVoteEvent(newevent);
        VoteTransaction vt1=wallet4.vote("null1", "Event1");
        VoteTransaction vt2=wallet3.vote("null1", "Event1");
        VoteTransaction vt3=wallet4.vote("null1", "Event1");
        
        Block block2=new Block(1,block.getHash(),3);
        
        System.out.println("TransactionHash: "+vt2.calculateTransactionHash());
        System.out.println("Block hash: "+ block.calculateHash());
        boolean accepted1=block2.addVoteTransaction(vt1, chain);
        boolean accepted2=block2.addVoteTransaction(vt2, chain);
        boolean accepted3=block2.addVoteTransaction(vt3, chain);
        block2.mineBlock();
        chain.addBlock(block2);
        
        System.out.println("Accepted: "+accepted1+" "+accepted2+" "+accepted3);

        Node node1=nodes.get(0);
        boolean chain_isValid=node1.setBlockchain(chain);
        for (int i = 1; i < nodes.size(); i++) {
            nodes.get(i).getBlockChainFromNetwork();
            System.out.println(i);
        }
        System.out.println("Chain is valid: "+chain_isValid);
        for (Node node : nodes) {
            System.out.println(node.getBlockChain().equals(chain));
        }

        ArrayList<VoterVerificationTransaction> vvts=new ArrayList<>();
        for (Wallet w: wallets) {
            if(!w.equals(wallets.get(1))){
                VoterVerificationTransaction vvt=w.verifyAWallet(wallets.get(1).getKeypair().getPublicKey(),"Nicosia");
                vvts.add(vvt);
            }
            if(!w.equals(wallets.get(2))){
                VoterVerificationTransaction vvt=w.verifyAWallet(wallets.get(2).getKeypair().getPublicKey(),"Nicosia");
                vvts.add(vvt);
            }
            if(!w.equals(wallets.get(6))){
                VoterVerificationTransaction vvt=w.verifyAWallet(wallets.get(6).getKeypair().getPublicKey(),"Nicosia");
                vvts.add(vvt);
            }
        }
        VoteEvent event=wallets.get(0).voteEvent("Nicosia", "vv", 100000000);
        event.addOption("Option1");
        event.addOption("Option2");
        event.addOption("Option3");
        event.addOption("Option4");
        event.addOption("Option5");

        for (VoterVerificationTransaction vvt : vvts) {
            node1.sendVvt(vvt);
        }
        // node1.getBlockChainFromNetwork();

        System.out.println("How many know: "+node1.getBlockChain().howmanyknow(wallets.get(6).getKeypair().getPublicKey()));

        


        node1.sendVE(event);
        for (int i=0; i<wallets.size()-1; i++){
            VoteTransaction vt=wallets.get(i).vote("Option1", "vv");
            // for (Node node : nodes){
            //     node.sendVt(vt);
            // }
            node1.sendVt(vt);
        }
        

        node1.getBlockChainFromNetwork();
        


        VoteTransaction vt=wallets.get(6).vote("Option4", "vv");
        node1.sendVt(vt);

        event=wallets.get(0).voteEvent("Nicosia", "vv1", 100000000);
        event.addOption("Option1");
        event.addOption("Option2");
        event.addOption("Option3");
        event.addOption("Option4");
        event.addOption("Option5");
        node1.sendVE(event);
        event=wallets.get(0).voteEvent("Nicosia", "vv2", 100000000);
        event.addOption("Option1");
        event.addOption("Option2");
        event.addOption("Option3");
        event.addOption("Option4");
        event.addOption("Option5");
        node1.sendVE(event);
        event=wallets.get(0).voteEvent("Nicosia", "vv3", 100000000);
        event.addOption("Option1");
        event.addOption("Option2");
        event.addOption("Option3");
        event.addOption("Option4");
        event.addOption("Option5");
        node1.sendVE(event);

        
        node1.getBlockChainFromNetwork();

        for (Node node : nodes) {
            var results=node.getBlockChain().getEventResults("vv");
            System.out.println(results);
        }

        // System.out.println(chain_isValid);
        // System.out.println(node1.getChain().toString());
    }

    
}
