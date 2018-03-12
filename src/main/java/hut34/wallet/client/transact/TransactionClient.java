package hut34.wallet.client.transact;

public interface TransactionClient {

    String sendSignedTransaction(String signedPayload);

}
