package me.bvn13.lesson.camel.testing.cameltesting;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestPropertyProviderImpl implements PropertiesProvider {
    @Override
    public String getInputEndpoint() {
        return "direct://start";
    }

    @Override
    public String getOutputEndpoint() {
        return "mock://finish";
    }

    @Override
    public String getSupervisorEndpoint() {
        return "mock://supervisor";
    }
}
