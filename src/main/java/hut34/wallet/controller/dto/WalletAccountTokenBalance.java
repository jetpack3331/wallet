package hut34.wallet.controller.dto;

public class WalletAccountTokenBalance extends WalletAccountBalance {
    private final String contractAddress;

    public WalletAccountTokenBalance(String contractAddress, String address, String balance) {
        super(address, balance);
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

}
