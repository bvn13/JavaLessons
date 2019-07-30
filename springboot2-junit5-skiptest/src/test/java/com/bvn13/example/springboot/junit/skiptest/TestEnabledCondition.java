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
            String prefix = null;

            Optional<TestEnabledPrefix> classAnnotationPrefix = context.getTestClass().map(cl -> cl.getAnnotation(TestEnabledPrefix.class));
            if (classAnnotationPrefix.isPresent()) {
                prefix = classAnnotationPrefix.get().prefix();
            }

            if (prefix != null && !prefix.isEmpty() && !prefix.endsWith(".")) {
                prefix += ".";
            } else {
                prefix = "";
            }

            String property = annotation.get().property();
            if (property.isEmpty()) {
                return ConditionEvaluationResult.disabled("Disabled - property not set!");
            }
            Boolean value = environment.getProperty(prefix + property, Boolean.class);
            if (value == null) {
                return ConditionEvaluationResult.disabled("Disabled - property <"+property+"> not set!");
            } else if (Boolean.TRUE.equals(value)) {
                return ConditionEvaluationResult.enabled("Enabled by property: "+property);
            } else {
                return ConditionEvaluationResult.disabled("Disabled by property: "+property);
            }
        }
        return ConditionEvaluationResult.enabled("Enabled by default");
    }
}
