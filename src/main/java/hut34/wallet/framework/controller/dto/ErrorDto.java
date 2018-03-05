package hut34.wallet.framework.controller.dto;

import java.util.Arrays;

public class ErrorDto {
    private String message;
    private TraceElement[] trace = new TraceElement[0];

    public String getMessage() {
        return message;
    }

    public ErrorDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public StackTraceElement[] getStackTrace() {
        return Arrays.stream(trace)
            .map(element -> new StackTraceElement(element.getClazz(), element.getMethod(), element.getFile(), element.getLine()))
            .toArray(StackTraceElement[]::new);
    }

    public TraceElement[] getTrace() {
        return trace;
    }

    public ErrorDto setTrace(TraceElement[] trace) {
        this.trace = trace;
        return this;
    }

    private static class TraceElement {
        public TraceElement() {
        }

        public String getFile() {
            return file;
        }

        public String getMethod() {
            return method;
        }

        public String getClazz() {
            return clazz;
        }

        public int getLine() {
            return line;
        }

        private String file;
        private String method;
        private String clazz;
        private int line;
    }
}
