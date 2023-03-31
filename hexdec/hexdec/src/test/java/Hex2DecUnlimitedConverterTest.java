import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class Hex2DecUnlimitedConverterTest {
    @Test
    void test() {
        Hex2DecUnlimitedConverter converter = new Hex2DecUnlimitedConverter();
        Assertions.assertEquals(BigInteger.valueOf(9223372036854775807L), converter.convert("7fffffffffffffff"));
        Assertions.assertEquals(new BigInteger("170141183460469231722463931679029329919"), converter.convert("7FFFFFFFFFFFFFFF7FFFFFFFFFFFFFFF"));
    }
}
