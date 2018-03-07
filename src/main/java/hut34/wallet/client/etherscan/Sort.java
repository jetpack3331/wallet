package hut34.wallet.client.etherscan;

public enum Sort {
    ASC("asc"), DESC("desc");

    private final String paramValue;

    Sort(String paramValue) {
        this.paramValue = paramValue;
    }

    public String paramValue() {
        return paramValue;
    }
}
