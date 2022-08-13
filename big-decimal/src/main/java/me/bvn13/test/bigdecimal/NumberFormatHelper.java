package me.bvn13.test.bigdecimal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

public class NumberFormatHelper {

    public static String currencyAmount(BigDecimal amount, Currency currency) {
        final DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        return df.format(amount);
    }

}
