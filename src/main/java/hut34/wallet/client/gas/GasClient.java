package hut34.wallet.client.gas;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class GasClient {

    private final RestOperations restOperations;

    public GasClient(RestTemplateBuilder restTemplateBuilder) {
        this.restOperations = restTemplateBuilder.build();
    }

    public GasInfo getGasInfo() {
        return restOperations.getForObject("https://ethgasstation.info/json/ethgasAPI.json", GasInfo.class);
    }

}
