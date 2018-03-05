package hut34.wallet.framework;

import org.hamcrest.Matcher;
import org.junit.rules.ExternalResource;
import hut34.wallet.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import static java.util.logging.Logger.getLogger;
import static org.hamcrest.CoreMatchers.is;

/**
 * Capture logs for test assertion. Specifically for Java Util Logging.
 */
public class MockJdkLogging extends ExternalResource implements MockLogging {
    private final Logger log;
    private LogRecordingHandler customLogHandler;

    /**
     * Creates a rule that captures logs from every logger. This could get noisy.
     * Consider on of the other constructors for a more targeted test.
     */
    public MockJdkLogging() {
        this(Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * Creates a rule that captures logs for the logger with the specified class's name.
     *
     * @param clazz The class for the logger.
     */
    public MockJdkLogging(Class<?> clazz) {
        this(clazz.getName());
    }

    /**
     * Creates a rule that captures logs for the logger with the specified name.
     *
     * @param loggerName The name for the logger.
     */
    public MockJdkLogging(String loggerName) {
        log = getLogger(loggerName);
    }

    @Override
    protected void before() {
        customLogHandler = new LogRecordingHandler(new SimpleFormatter());
        log.addHandler(customLogHandler);
    }

    @Override
    protected void after() {
        log.removeHandler(customLogHandler);
    }

    /**
     * Clear out any existing logs and start again.
     */
    public void reset() {
        after();
        before();
    }

    public String getLoggedString() {
        return customLogHandler.logRecords.stream()
            .map(FormattedLogRecord::getFormattedMessage)
            .collect(Collectors.joining("\n"));
    }

    public void assertMessage(Level expectedLevel, String expectedMessage) {
        assertMessage(expectedLevel, is(expectedMessage));
    }

    public void assertMessage(Level expectedLevel, Matcher matcher) {
        Assert.isTrue(customLogHandler.matchRawMessage(expectedLevel, matcher).isPresent(), "Log message does not exist.");
    }

    /**
     * Log handler that keeps a list of LogRecords in memory.
     */
    private class LogRecordingHandler extends Handler {

        private List<FormattedLogRecord> logRecords = new ArrayList<>();

        LogRecordingHandler(Formatter formatter) {
            super();
            setFormatter(formatter);
        }

        @Override
        public void publish(LogRecord record) {
            logRecords.add(new FormattedLogRecord(record, getFormatter().format(record)));
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }

        Optional<FormattedLogRecord> matchRawMessage(Level expectedLevel, Matcher matcher) {
            return matchMessage(expectedLevel, matcher, FormattedLogRecord::getMessage);
        }

        Optional<FormattedLogRecord> matchFormattedMessage(Level expectedLevel, Matcher matcher) {
            return matchMessage(expectedLevel, matcher, FormattedLogRecord::getFormattedMessage);
        }

        Optional<FormattedLogRecord> matchMessage(Level expectedLevel, Matcher matcher, Function<FormattedLogRecord, String> msgFunction) {
            return logRecords.stream()
                .filter(event -> event.getLevel() == expectedLevel && matcher.matches(msgFunction.apply(event)))
                .findFirst();
        }
    }

    /**
     * LogRecord that stored raw data and formatted log message.
     */
    private class FormattedLogRecord extends LogRecord {

        private String formattedMessage;

        FormattedLogRecord(LogRecord logRecord, String formattedMessage) {
            super(logRecord.getLevel(), logRecord.getMessage());
            this.formattedMessage = formattedMessage;
        }

        public String getFormattedMessage() {
            return formattedMessage;
        }
    }
}
