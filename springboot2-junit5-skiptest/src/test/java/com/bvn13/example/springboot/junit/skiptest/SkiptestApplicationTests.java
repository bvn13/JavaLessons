package com.bvn13.example.springboot.junit.skiptest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SkiptestApplicationTests {

    @TestEnabled(property = "app.skip.test.first")
    @Test
    public void testFirst() {
        assertTrue(true);
    }

    @TestEnabled(property = "app.skip.test.second")
    @Test
    public void testSecond() {
        assertTrue(false);
    }

}
