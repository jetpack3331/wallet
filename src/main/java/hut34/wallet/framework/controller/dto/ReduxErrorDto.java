package hut34.wallet.framework.controller.dto;

public class ReduxErrorDto extends ErrorDto {
    private String type;
    private String lastAction;

    public ReduxErrorDto() {
    }

    public String getLastAction() {
        return lastAction;
    }

    public String getType() {
        return type;
    }

    public ReduxErrorDto setType(String type) {
        this.type = type;
        return this;
    }

    public ReduxErrorDto setLastAction(String lastAction) {
        this.lastAction = lastAction;
        return this;
    }
}
