package hut34.wallet.controller.dto;

import hut34.wallet.framework.BaseDto;
import hut34.wallet.model.WalletAccountType;

public class WalletAccountDto extends BaseDto {
    private WalletAccountType type;
    private String address;
    private String secretStorageJson;

    public WalletAccountType getType() {
        return type;
    }

    public void setType(WalletAccountType type) {
        this.type = type;
    }

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
