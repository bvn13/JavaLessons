package me.bvn13.test.bigdecimal;

import java.math.BigDecimal;
import java.util.Currency;

public class BigDecimalExample {
    public static void main(String[] args) {
//        fromInteger();
//        fromFloat();
//        a();
//        b();

//        Currency eur = Currency.getInstance("EUR");
//        System.out.println("EUR: " + eur.getDefaultFractionDigits());
//        Currency tnd = Currency.getInstance("TND");
//        System.out.println("TND:" + tnd.getDefaultFractionDigits());

//        BigDecimal amount = new BigDecimal("4.12");
//        System.out.println(amount);
//        amount = amount.movePointRight(2);
//        System.out.println(amount);
//        amount = amount.movePointLeft(2);
//        System.out.println(amount);

        BigDecimal amount = new BigDecimal("4.12");
        Currency eur = Currency.getInstance("EUR");
        System.out.println(amount + " " + eur);
        MoneyDto moneyDto = new MoneyDto(amount, eur);
        System.out.println(moneyDto);

        MoneyDto incomingUsd = MoneyDto.fromJson("514", "USD");
        System.out.println(incomingUsd + " -> " + incomingUsd.getAmount() + " " + incomingUsd.getCurrency());
        MoneyDto incomingTnd = MoneyDto.fromJson("5148", "TND");
        System.out.println(incomingTnd + " -> " + incomingTnd.getAmount() + " " + incomingTnd.getCurrency());
    }

    static void fromInteger() {
        BigDecimal amount = new BigDecimal(5);
        System.out.println(amount);
    }

    static void fromFloat() {
        BigDecimal amount = new BigDecimal(5.123456789);
        System.out.println(amount);
    }

    static void a() {
        BigDecimal amount = new BigDecimal("5");
        System.out.println(amount);
    }

    static void b() {
        BigDecimal amount = new BigDecimal("5.123456789");
        System.out.println(amount);
    }
}
