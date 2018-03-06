package hut34.wallet.controller.dto;

public class CreateWalletRequest {
    private String address;
    private String encryptedPrivateKey;

    public CreateWalletRequest() {
    }

    public CreateWalletRequest(String address, String encryptedPrivateKey) {
        this.address = address;
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

}
