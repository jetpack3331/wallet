package hut34.wallet.testinfra;

import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.model.WalletAccount;

import java.util.UUID;

public class TestData {

    public static User user() {
        return user("admin@3wks.com.au");
    }

    public static User user(String email) {
        return User.byEmail(email, "myPass");
    }

    public static WalletAccount walletAccount(String address) {
        return new WalletAccount(address, user());
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}
