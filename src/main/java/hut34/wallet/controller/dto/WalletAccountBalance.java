package hut34.wallet.controller.dto;

public class WalletAccountBalance {
    private final String address;
    private final String balance;

    public WalletAccountBalance(String address, String balance) {
        this.address = address;
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public String getBalance() {
        return balance;
    }
}
