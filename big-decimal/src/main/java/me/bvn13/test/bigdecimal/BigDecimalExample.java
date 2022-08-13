package me.bvn13.test.bigdecimal;

import java.math.BigDecimal;
import java.util.Currency;

public class BigDecimalExample {
    public static void main(String[] args) {
//        fromInteger();
//        fromFloat();
//        a();
//        b();

        Currency eur = Currency.getInstance("EUR");
        System.out.println("EUR: " + eur.getDefaultFractionDigits());
        Currency tnd = Currency.getInstance("TND");
        System.out.println("TND:" + tnd.getDefaultFractionDigits());
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
