// import java.rmi.RemoteException;
// import java.util.HashMap;

// import com.mycompany.blockchainvoting.NodeP2P.Node;
// // import com.mycompany.blockchainvoting.P2PNetwork.Miner;
// // import com.mycompany.blockchainvoting.P2PNetwork.Node;
// // import com.mycompany.blockchainvoting.P2PNetwork.NodeImpl;
// import com.mycompany.blockchainvoting.NodeP2P.NodeInterface;
// import com.mycompany.blockchainvoting.Blockchain.Block;
// import com.mycompany.blockchainvoting.Blockchain.Blockchain;
// import com.mycompany.blockchainvoting.Blockchain.VoteEvent;
// import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;
// import com.mycompany.blockchainvoting.Blockchain.VoterVerificationTransaction;
// import com.mycompany.blockchainvoting.Blockchain.Wallet;

// import java.rmi.RemoteException;
// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.NoSuchAlgorithmException;
// import java.security.PublicKey;
// import java.util.concurrent.Semaphore;

// /**
//  *
//  * @author charalambos
//  */

// public class testAll {
//             public static void main(String[] args) {
//                 try {
//                     testNodes();
//                 } catch (RemoteException | NoSuchAlgorithmException e) {
//                     e.printStackTrace();
//                 }
//             }
        
//             public static void testNodes() throws RemoteException, NoSuchAlgorithmException {
//                 KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//                 keyPairGenerator.initialize(2048);
//                 KeyPair keyPairA = keyPairGenerator.generateKeyPair();
//                 KeyPair keyPairB = keyPairGenerator.generateKeyPair();
        
//                 PublicKey publicKeyA = keyPairA.getPublic();
//                 PublicKey publicKeyB = keyPairB.getPublic();
        
//                 Semaphore semaphore = new Semaphore(1);
//                 Node nodeA = new Node("NodeA", publicKeyA, semaphore, false);
//                 Node nodeB = new Node("NodeB", publicKeyB, semaphore, true);
        
//                 System.out.println("NodeA: " + nodeA.getName());
//                 System.out.println("NodeB: " + nodeB.getName());
        
//                 System.out.println("NodeA is miner: " + nodeA.isMiner);
//                 System.out.println("NodeB is miner: " + nodeB.isMiner);
        
//                 nodeA.addNodesink(nodeB);
//                 nodeB.addNodesource(nodeA);
        
//                 System.out.println("Nodesink size of NodeA: " + nodeA.nodesink.size());
//                 System.out.println("Nodesource size of NodeB: " + nodeB.nodesource.size());
        
//                 nodeA.send("Hello, NodeB!");
        
//                 Blockchain newChain = new Blockchain();
//                 Block genesisBlock = newChain.getGenesisBlock();
//                 System.out.println("Blockchain set for NodeA: " + nodeA.setBlockchain(newChain));
        
//                 nodeA.sendChain(newChain);
//                 nodeA.getBlockChainFromNetwork();
        
//                 System.out.println("NodeB's Genesis Block Hash: " + nodeB.getBlockChain().getGenesisBlock().getHash());
//                 Wallet w= new Wallet();
//                 Wallet w1=new Wallet();

//                 w.verifyAWallet(publicKeyB, "New York");
//                 w1.verifyAWallet(publicKeyB, "New York");
//                 VoterVerificationTransaction vvt = 
//                 nodeA.sendVvt(vvt);
        
//                 VoteTransaction vt = 
//                 nodeA.sendVt(vt);
        
//                 VoteEvent ve = new VoteEvent(publicKeyA, "Event2", "San Francisco", 3);
//                 nodeA.sendVE(ve);
//             }
//         }
        
 

//     }
// }
