import java.math.BigDecimal;
import java.math.BigInteger;

public class Hex2DecUnlimitedConverter {
    public BigInteger convert(String hex) {
        BigInteger result = BigInteger.ZERO;
        for (int pos = hex.length() - 1, pow = 0; pos >= 0; pos--, pow++) {
           int value = take(hex.charAt(pos));
           result = result.add(BigInteger.valueOf(value).multiply(BigInteger.valueOf(16L).pow(pow)));
        }
        return result;
    }

    private int take(char digit) {
        return switch (digit) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'A' -> 10;
            case 'a' -> 10;
            case 'B' -> 11;
            case 'b' -> 11;
            case 'C' -> 12;
            case 'c' -> 12;
            case 'D' -> 13;
            case 'd' -> 13;
            case 'E' -> 14;
            case 'e' -> 14;
            case 'F' -> 15;
            case 'f' -> 15;
            default -> throw new IllegalArgumentException("Not a HEX: " + digit);
        };
    }
}
