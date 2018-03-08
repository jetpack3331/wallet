package hut34.wallet.controller.dto;

public class WalletAccountBalance {
    private final String balance;

    public WalletAccountBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }
}
