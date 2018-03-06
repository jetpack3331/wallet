package hut34.wallet.controller.dto;

import hut34.wallet.framework.BaseDto;

public class WalletAccountDto extends BaseDto {
    private String address;
    private String encryptedPrivateKey;

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
