package hut34.wallet.framework;

import org.junit.rules.TestRule;

/**
 * Capture logs for test assertion.
 */
public interface MockLogging extends TestRule {

    /**
     * Reset state to discard and logs captured. This is useful to exclude any logs written during setup
     * of a test. Call this just before actual method invocation.
     *
     */
    void reset();

    /**
     * Return all logs captured as a single String.
     *
     * @return all logs as one string since creation/reset.
     */
    String getLoggedString();

}
