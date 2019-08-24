package com.bvn13.example.springboot.junit.skiptest;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

/**
 * @author bvn13
 * @since 28.07.2019
 */
public class TestEnabledCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Environment environment = SpringExtension.getApplicationContext(context).getEnvironment();

        return context.getElement()
                .map(e -> e.getAnnotation(TestEnabled.class))
                .map(annotation -> {
                    String property = annotation.property();

                    String prefix = context.getTestClass()
                            .map(cl -> cl.getAnnotation(TestEnabledPrefix.class))
                            .map(pref -> {
                                if (!pref.prefix().isEmpty() && !pref.prefix().endsWith(".")) {
                                    return pref.prefix()+".";
                                } else {
                                    return "";
                                }
                            }).orElse("");

                    return Optional.ofNullable(environment.getProperty(prefix + property, Boolean.class))
                            .map(value -> {
                                if (Boolean.TRUE.equals(value)) {
                                    return ConditionEvaluationResult.enabled("Enabled by property: "+property);
                                } else {
                                    return ConditionEvaluationResult.disabled("Disabled by property: "+property);
                                }
                            }).orElse(
                                    ConditionEvaluationResult.disabled("Disabled - property <"+property+"> not set!")
                            );
                }).orElse(
                        ConditionEvaluationResult.enabled("Enabled by default")
                );

    }
}
