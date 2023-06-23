/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.P2PNetwork;

import java.util.ArrayList;

import com.mycompany.blockchainvoting.Blockchain.VoteTransaction;

/**
 *
 * @author charalambos
 */
public class Miner {
    
    private String address;  // The address of the miner's wallet
    private int nonce;  // The nonce value used in mining
    private ArrayList<VoteTransaction> mempool;  // The miner's transaction mempool
    
    
    public Miner(String address) {
        this.address = address;
        this.nonce = 0;
        this.mempool = new ArrayList<>();
    }
    
    public String getAddress() {
        return this.address;
    }
    public int getNonce() {
        return this.nonce;
    }    
    public void addTransaction(VoteTransaction tx) {
        this.mempool.add(tx);
    }
    
    
    
}
