package hut34.wallet.controller.dto;

import hut34.wallet.framework.BaseDto;

public class WalletAccountDto extends BaseDto {
    private String address;
    private String secretStorageJson;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSecretStorageJson() {
        return secretStorageJson;
    }

    public void setSecretStorageJson(String secretStorageJson) {
        this.secretStorageJson = secretStorageJson;
    }

}
