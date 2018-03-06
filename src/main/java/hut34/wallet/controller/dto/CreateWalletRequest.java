package hut34.wallet.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CreateWalletRequest {
    @NotBlank
    private String address;
    @NotBlank
    private String secretStorageJson;

    public CreateWalletRequest() {
    }

    public CreateWalletRequest(String address, String secretStorageJson) {
        this.address = address;
        this.secretStorageJson = secretStorageJson;
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
