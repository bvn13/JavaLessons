package com.bvn13.example.springboot.junit.skiptest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bvn13
 * @since 26.07.2019
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestEnabledPrefix {
    String prefix();
}
