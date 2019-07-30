package com.bvn13.example.springboot.junit.skiptest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestEnabledPrefix(property = "app.skip.test")
public class SkiptestApplicationTests {

    @TestEnabled(property = "first")
    @Test
    public void testFirst() {
        assertTrue(true);
    }

    @TestEnabled(property = "second")
    @Test
    public void testSecond() {
        assertTrue(false);
    }

}
