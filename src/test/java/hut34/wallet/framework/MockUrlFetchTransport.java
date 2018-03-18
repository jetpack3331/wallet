package hut34.wallet.framework;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MockUrlFetchTransport extends MockHttpTransport {

    private List<MockLowLevelHttpRequest> requestLog = new ArrayList<>();
    private Queue<MockLowLevelHttpResponse> nextResponses = new LinkedList<>();

    public void addResponse(MockLowLevelHttpResponse response) {
        this.nextResponses.add(response);
    }

    public List<MockLowLevelHttpRequest> getRequestLog() {
        return requestLog;
    }

    public MockLowLevelHttpRequest getLastRequest() {
        return requestLog.get(requestLog.size() - 1);
    }

    public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
        MockLowLevelHttpRequest request = (MockLowLevelHttpRequest) super.buildRequest(method, url);
        if (!nextResponses.isEmpty()) {
            request.setResponse(nextResponses.remove());
        }
        requestLog.add(request);
        return request;
    }
}
