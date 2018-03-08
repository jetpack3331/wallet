package hut34.wallet.controller.dto;

import hut34.wallet.client.etherscan.model.Transaction;

import java.util.List;

public class WalletAccountTransactions {
    private final String address;
    private final List<Transaction> transactions;


    public WalletAccountTransactions(String address, List<Transaction> transactions) {
        this.address = address;
        this.transactions = transactions;
    }

    public String getAddress() {
        return address;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}
