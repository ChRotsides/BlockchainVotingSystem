/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.blockchainvoting.Blockchain;

/**
 *
 * @author charalambos
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    public static String calculateMerkleRoot_vt(List<VoteTransaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return null;
        }
        List<String> tempTxList = new ArrayList<>();
        for (VoteTransaction transaction : transactions) {
            tempTxList.add(transaction.calculateTransactionHash());
        }

        while (tempTxList.size() > 1) {
            List<String> updatedTxList = new ArrayList<>();

            for (int i = 0; i < tempTxList.size(); i += 2) {
                if (i + 1 < tempTxList.size()) {
                    String data=tempTxList.get(i) + tempTxList.get(i + 1);
                    updatedTxList.add(sha256(data));
                } else {
                    updatedTxList.add(tempTxList.get(i));
                }
            }

            tempTxList = updatedTxList;
        }

        return tempTxList.get(0);
    }

    public static String calculateMerkleRoot_vvt(List<VoterVerificationTransaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return null;
        }
        List<String> tempTxList = new ArrayList<>();
        for (VoterVerificationTransaction transaction : transactions) {
            tempTxList.add(transaction.calculateTransactionHash());
        }

        while (tempTxList.size() > 1) {
            List<String> updatedTxList = new ArrayList<>();

            for (int i = 0; i < tempTxList.size(); i += 2) {
                if (i + 1 < tempTxList.size()) {
                    String data=tempTxList.get(i) + tempTxList.get(i + 1);
                    updatedTxList.add(sha256(data));
                } else {
                    updatedTxList.add(tempTxList.get(i));
                }
            }

            tempTxList = updatedTxList;
        }

        return tempTxList.get(0);
    }
    private static String sha256(String data) {
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
}
