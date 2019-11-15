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
    public void createAmountLine() {
        assertThat(StringFormatter.createAmountLine(215.39, Constants.LABEL_TOTAL), equalTo(Constants.LABEL_TOTAL + "                                    215,39\r\n"));
        assertThat(StringFormatter.createAmountLine(215.39, Constants.LABEL_PAID), equalTo(Constants.LABEL_PAID + "                                   215,39\r\n"));
        assertThat(StringFormatter.createAmountLine(215.39, Constants.LABEL_RETURN), equalTo(Constants.LABEL_RETURN + "                                     215,39\r\n"));
        assertThat(StringFormatter.createAmountLine(215.39, Constants.LABEL_PAID_BY_BANCONTACT), equalTo(Constants.LABEL_PAID_BY_BANCONTACT + "                    215,39\r\n"));
        assertThat(StringFormatter.createAmountLine(215.39, Constants.LABEL_PAID_BY_PAYCONIQ), equalTo(Constants.LABEL_PAID_BY_PAYCONIQ + "                      215,39\r\n"));
    }

    @Test
    public void createStarLine() {
        assertThat(StringFormatter.createStarLine(), equalTo("************************************************\r\n"));
    }
}