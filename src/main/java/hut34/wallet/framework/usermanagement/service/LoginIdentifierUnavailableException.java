package hut34.wallet.framework.usermanagement.service;


public class LoginIdentifierUnavailableException extends RuntimeException {
    public LoginIdentifierUnavailableException(String loginIdentifier) {
        super(String.format("Login ID %s is unavailable", loginIdentifier));
    }
}
