package hut34.wallet.client.etherscan.model;

import java.util.ArrayList;
import java.util.List;

public class ListResponse<T> extends Response<List<T>> {

    public ListResponse() {
        setResponse(new ArrayList<>());
    }

}
