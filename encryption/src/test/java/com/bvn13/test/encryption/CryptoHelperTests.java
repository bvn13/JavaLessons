package com.bvn13.test.encryption;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.crypto.BadPaddingException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author bvn13
 * @since 13.09.2019
 */
@RunWith(JUnit4.class)
public class CryptoHelperTests {

    @Test
    public void testEncryptionWithPassword() throws Exception {
        test("12345");
    }

    @Test
    public void testRepeatingEncryptionWithPassword() throws Exception {
        for (int i=0; i<50; i++) {
            test(UUID.randomUUID().toString());
        }
    }

    private void test(String password) throws Exception {
        String test = UUID.randomUUID().toString();

        String enc = CryptoHelper.encrypt(test, password);
        String dec = CryptoHelper.decrypt(enc, password);

        assertEquals(test, dec);

        try {
            CryptoHelper.decrypt(enc, password+"1");
        } catch (Exception e) {
            assertTrue(e instanceof BadPaddingException);
        }
    }

}
