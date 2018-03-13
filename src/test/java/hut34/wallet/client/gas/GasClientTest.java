package hut34.wallet.client.gas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ActiveProfiles("junit")
@RestClientTest(GasClient.class)
public class GasClientTest {
    private static final String SAMPLE_API_RESPONSE = "{\"average\": 21.0, \"average_calc\": 40.0, \"safelow_calc\": 10.0, \"blockNum\": 5240066, \"safeLow\": 20.0, \"safeLowWait\": 5.6, \"fastest\": 200.0, \"speed\": 0.801914713115816, \"fastestWait\": 0.4, \"fastWait\": 0.4, \"block_time\": 13.637755102040817, \"avgWait\": 5.6, \"safelow_txpool\": 20.0, \"average_txpool\": 20.0, \"fast\": 60.0}";

    @Autowired
    private GasClient client;
    @Autowired
    private MockRestServiceServer server;


    @Test
    public void getGasInfo() {
        server.expect(requestTo("https://ethgasstation.info/json/ethgasAPI.json"))
            .andRespond(withSuccess(SAMPLE_API_RESPONSE, MediaType.APPLICATION_JSON));

        GasInfo gasInfo = client.getGasInfo();

        assertThat(gasInfo.getAveragePrice(), is("2100000000"));
        assertThat(gasInfo.getSafeLowPrice(), is("2000000000"));
        assertThat(gasInfo.getFastPrice(), is("6000000000"));
        assertThat(gasInfo.getFastestPrice(), is("20000000000"));
    }

}
