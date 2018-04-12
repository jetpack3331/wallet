package hut34.wallet.util;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PasswordGeneratorTest {

    @Test
    public void alphanumeric_willGeneratePasswordOfSpecifiedLength() {
        assertThat(PasswordGenerator.alphanumeric(10).length(), is(10));
        assertThat(PasswordGenerator.alphanumeric(21).length(), is(21));
        assertThat(PasswordGenerator.alphanumeric(26).length(), is(26));
        assertThat(PasswordGenerator.alphanumeric(32).length(), is(32));
    }

    @Test
    public void alphanumeric_willGenerateAlphanumericPassword() {
        for (int x = 0; x < 10000; x++) {
            assertThat(PasswordGenerator.alphanumeric(10).matches("^[a-zA-Z0-9]{10}$"), is(true));
        }
    }

    @Test
    public void alphanumeric_willGenerateRandomPassword() {
        // Hard to test randomness and random != unique but assume that
        // for good implementation passwords shouldn't repeat very often
        String password = PasswordGenerator.alphanumeric(10);
        for (int x = 0; x < 10000; x++) {
            assertThat(password, not(PasswordGenerator.alphanumeric(10)));
        }
    }
}
