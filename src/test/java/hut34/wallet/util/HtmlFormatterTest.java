package hut34.wallet.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HtmlFormatterTest {

    private HtmlFormatter formatter;

    @Before
    public void before() {
        formatter = new HtmlFormatter();
    }

    @Test
    public void toPlainText_willConvertHtml() {
        String html = "<p>Hi</p>" +
            "<p>You have a proof to view, approve or reject.</p>" +
            "<p><a href=\"http://www.thegoogles.com/\" target=\"_blank\">View the proof now.</a></p>";

        String result = formatter.toPlainText(html);

        assertThat(result, is("\n" +
            "Hi\n" +
            "\n" +
            "You have a proof to view, approve or reject.\n" +
            "\n" +
            "View the proof now. <http://www.thegoogles.com/>\n"));
    }

    @Test
    public void toPlainText_willConvertAndWordWrap_whenPlainText() {
        String plainText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis efficitur odio eu ex porta interdum. Suspendisse ac libero eget metus vulputate elementum. " +
            "Pellentesque a ligula aliquam, congue tellus non, luctus ante. Quisque eleifend sem ut felis elementum suscipit. Aliquam volutpat ultricies justo vitae consequat. Quisque erat turpis, " +
            "luctus at turpis non, eleifend scelerisque risus.";

        String result = formatter.toPlainText(plainText);

        assertThat(result, is("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis efficitur odio eu \n" +
            "ex porta interdum. Suspendisse ac libero eget metus vulputate elementum. \n" +
            "Pellentesque a ligula aliquam, congue tellus non, luctus ante. Quisque eleifend \n" +
            "sem ut felis elementum suscipit. Aliquam volutpat ultricies justo vitae \n" +
            "consequat. Quisque erat turpis, luctus at turpis non, eleifend scelerisque \n" +
            "risus."));
    }

}
