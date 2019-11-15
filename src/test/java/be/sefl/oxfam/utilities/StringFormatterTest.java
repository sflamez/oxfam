package be.sefl.oxfam.utilities;

import be.sefl.oxfam.constants.Constants;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StringFormatterTest {

    @Test
    public void center() {
        assertThat(StringFormatter.center("215,39"), equalTo("                     215,39\r\n"));
    }

    @Test
    public void formatCount() {
        assertThat(StringFormatter.formatCount(17), equalTo("                            17x "));
    }

    @Test
    public void formatDescription() {
        assertThat(StringFormatter.formatDescription("Blie bla bloe"), equalTo("Blie bla bloe                           "));
    }

    @Test
    public void formatUnitPrice() {
        assertThat(StringFormatter.formatUnitPrice("215,39"), equalTo("215,39  "));
    }

    @Test
    public void formatTotalPrice() {
        assertThat(StringFormatter.formatTotalPrice("215,39"), equalTo("  215,39"));
    }

    @Test
    public void formatTotalAmount() {
        assertThat(StringFormatter.formatAmount(215.39, Constants.LABEL_TOTAL), equalTo("                                    215,39"));
    }

    @Test
    public void formatPaidAmount() {
        assertThat(StringFormatter.formatAmount(215.39, Constants.LABEL_PAID), equalTo("                                   215,39"));
    }

    @Test
    public void formatReturnAmount() {
        assertThat(StringFormatter.formatAmount(215.39, Constants.LABEL_RETURN), equalTo("                                     215,39"));
    }

    @Test
    public void formatTotalBancontactAmount() {
        assertThat(StringFormatter.formatAmount(215.39, Constants.LABEL_PAID_BY_BANCONTACT), equalTo("                    215,39"));
    }

    @Test
    public void createStarLine() {
        assertThat(StringFormatter.createStarLine(), equalTo("************************************************\r\n"));
    }
}