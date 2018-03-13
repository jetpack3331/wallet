package hut34.wallet.client.transact;

import java.math.BigInteger;

public interface TransactionClient {

    String sendSignedTransaction(String signedPayload);

    BigInteger getNextNonce(String address);

}
