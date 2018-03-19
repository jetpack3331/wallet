package hut34.wallet.controller.dto;

import hut34.wallet.model.WalletAccountType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CreateWalletRequest {

    @NotNull
    private WalletAccountType type;
    private String address;
    private String secretStorageJson;

    public CreateWalletRequest() {
    }

    public CreateWalletRequest(WalletAccountType type) {
        this.type = type;
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

    @NotBlank
    public String getAddress() {
        return type == WalletAccountType.PRIVATE ? address : "MANAGED";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotBlank
    public String getSecretStorageJson() {
        return type == WalletAccountType.PRIVATE ? secretStorageJson : "MANAGED";
    }

    public void setSecretStorageJson(String secretStorageJson) {
        this.secretStorageJson = secretStorageJson;
    }
}
