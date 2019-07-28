package com.bvn13.example.springboot.junit.skiptest;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author bvn13
 * @since 28.07.2019
 */
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TestEnabledCondition.class)
public @interface TestEnabled {
    String property();
}
