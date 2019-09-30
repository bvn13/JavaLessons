package com.bvn13.example.recursion.print;

/**
 * @author bvn13
 * @since 21.09.2019
 */
public class RecursionPrint {

    public static void main(String... args) {

        long n = 20;

        boolean result = printIt(1, n);

    }

    private static boolean printIt(long x, long n) {
        System.out.println(x);

        return String.valueOf(x).equals(String.valueOf(n)) || printIt(x+1, n);
    }

}
