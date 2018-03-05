package hut34.wallet.framework.controller.dto;

public class ComponentErrorDto extends ErrorDto {
    private String componentStack;

    public ComponentErrorDto() {
    }

    public String getComponentStack() {
        return componentStack;
    }

}
