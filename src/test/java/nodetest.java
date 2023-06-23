import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import com.mycompany.blockchainvoting.Blockchain.Wallet;
import com.mycompany.blockchainvoting.Blockchain.keyPairHolder;
import com.mycompany.blockchainvoting.P2PNetwork.Node;

public class nodetest {
    public static void main(String[] args) throws NotBoundException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("Hello nodetest!");

        Wallet wallet=new Wallet();
        Wallet wallet2=new Wallet();
        Wallet wallet3=new Wallet();

        // File file=new File("wallets");

        FileWriter writer=new FileWriter("wallets.txt");
        String publicKeyString1 = Base64.getEncoder().encodeToString(wallet.getKeypair().getPublicKey().getEncoded());
        String publicKeyString2 = Base64.getEncoder().encodeToString(wallet2.getKeypair().getPublicKey().getEncoded());
        String publicKeyString3 = Base64.getEncoder().encodeToString(wallet3.getKeypair().getPublicKey().getEncoded());

        writer.write(publicKeyString1+"\n"+publicKeyString2+"\n"+publicKeyString3);
        writer.close();



        // Node
        Node node1=new Node(wallet.getKeypair().getPublicKey(),"localhost", 1090,1020);
        Node node2=new Node(wallet2.getKeypair().getPublicKey(), "localhost", 1020,1090);
        Node node3=new Node(wallet3.getKeypair().getPublicKey(), "localhost", 1021,1090);

        node1.setName("Node1");
        node2.setName("Node2");
        node3.setName("Node3");

        node1.startSource();
        node2.startSource();
        node3.startSource();
        node1.startSink();
        node2.startSink();
        node3.startSink();

        node1.sendMessage("Node1 says hello");
        node2.sendMessage("Node2 says hello");
        node3.sendMessage("Node3 says hello");


    }
}
