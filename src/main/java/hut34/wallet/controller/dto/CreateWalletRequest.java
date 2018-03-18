package hut34.wallet.controller.dto;

import hut34.wallet.model.WalletAccountType;
import org.hibernate.validator.constraints.NotBlank;

public class CreateWalletRequest {

    private WalletAccountType type;
    @NotBlank
    private String address;
    @NotBlank
    private String secretStorageJson;

    public CreateWalletRequest() {
    }

    public CreateWalletRequest(WalletAccountType type, String address, String secretStorageJson) {
        this.type = type;
        this.address = address;
        this.secretStorageJson = secretStorageJson;
    }

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
