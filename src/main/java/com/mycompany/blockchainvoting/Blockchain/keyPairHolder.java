/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author charalambos
 */
public class keyPairHolder {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    public keyPairHolder(PublicKey publickey, PrivateKey privatekey){
    
        this.privateKey=privatekey;
        this.publicKey=publickey;
    }
    
    public PublicKey getPublicKey(){
        return this.publicKey;
    }
    public PrivateKey getPrivateKey(){
        return this.privateKey;
    }
    
}
