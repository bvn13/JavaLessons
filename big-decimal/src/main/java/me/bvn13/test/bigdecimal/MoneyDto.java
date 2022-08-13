package me.bvn13.test.bigdecimal;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyDto {

    private final BigDecimal amount;
    private final Currency currency;

    public static MoneyDto fromMinor(BigDecimal amount, Currency currency) {
        return new MoneyDto(amount.movePointLeft(currency.getDefaultFractionDigits()), currency);
    }

    public static MoneyDto fromJson(String amount, String cur) {
        Currency currency = Currency.getInstance(cur);
        return new MoneyDto(new BigDecimal(amount).movePointLeft(currency.getDefaultFractionDigits()), currency);
    }

    public MoneyDto(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "{" +
                "\"amount\":" + amount.movePointRight(currency.getDefaultFractionDigits()) +
                ", \"currency\":\"" + currency + "\"" +
                '}';
    }
}
