package hut34.wallet.client.transact;

import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

@Component
public class Web3jHttpServiceBuilder implements Web3jServiceBuilder {

    @Override
    public Web3jService build(String url) {
        return new HttpService(url);
    }

}
