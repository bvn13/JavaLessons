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

        Optional<TestEnabled> annotation = context.getElement().map(e -> e.getAnnotation(TestEnabled.class));
        if (annotation.isPresent()) {
            String property = annotation.get().property();
            Boolean value = environment.getProperty(property, Boolean.class);
            if (Boolean.TRUE.equals(value)) {
                return ConditionEvaluationResult.enabled("Enabled by property: "+property);
            } else {
                return ConditionEvaluationResult.disabled("Disable by property: "+property);
            }
        }
        return ConditionEvaluationResult.enabled("Enabled by default");
    }
}
