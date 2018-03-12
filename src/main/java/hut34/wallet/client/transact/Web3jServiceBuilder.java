package hut34.wallet.client.transact;

import org.web3j.protocol.Web3jService;

public interface Web3jServiceBuilder {

    Web3jService build(String url);

}
