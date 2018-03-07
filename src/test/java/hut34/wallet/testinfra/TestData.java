package hut34.wallet.testinfra;

import hut34.wallet.framework.BaseEntityCore;
import hut34.wallet.framework.usermanagement.Role;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.model.WalletAccount;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

public class TestData {

    public static User user() {
        return user("admin@3wks.com.au");
    }

    public static User user(String email) {
        return User.byEmail(email, "myPass")
            .setRoles(Arrays.asList(Role.ADMIN, Role.SUPER));
    }

    public static WalletAccount walletAccount() {
        return walletAccount("0xASDFMEWIFREVNERIGVNERTIGRTNBRT");
    }

    public static WalletAccount walletAccount(String address) {
        return walletAccount(address, user());
    }

    public static WalletAccount walletAccount(String address, User owner) {
        return new WalletAccount(address, owner)
            .setSecretStorageJson("{\"address\":\"236cbcdf103a77f6401cf40cac1f22ca80b7aae4\",\"id\":\"b32249a2-eabe-41b7-a437-8a921609e19a\",\"version\":3,\"Crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"121c1a6a48b497216c940073523447c5\"},\"ciphertext\":\"911fcaf56a857d50327c0e00a9a46661fb9039580e67a3431ef4504698f70e92\",\"kdf\":\"scrypt\",\"kdfparams\":{\"salt\":\"a23d5b1e82bea9b1e6b15e3824fe1d8b6325e1be8e1494bfaf226cca0f09c122\",\"n\":131072,\"dklen\":32,\"p\":1,\"r\":8},\"mac\":\"154ef6aa209abe7db144ab009f6e8dd3c44a0ae524c74739c09a94386aeac8d0\"},\"x-ethers\":{\"client\":\"ethers.js\",\"gethFilename\":\"UTC--2018-03-06T23-44-51.0Z--236cbcdf103a77f6401cf40cac1f22ca80b7aae4\",\"mnemonicCounter\":\"fee63c5a1bfba23a8397a976375ae45b\",\"mnemonicCiphertext\":\"67539192aa8b1e400ecaba905f6725d1\",\"version\":\"0.1\"}}");
    }

    public static <T extends BaseEntityCore> T setCreatedUpdated(T entity) {
        ReflectionTestUtils.setField(entity, "created", OffsetDateTime.now().minusDays(1));
        ReflectionTestUtils.setField(entity, "updated", OffsetDateTime.now());
        return entity;
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}
