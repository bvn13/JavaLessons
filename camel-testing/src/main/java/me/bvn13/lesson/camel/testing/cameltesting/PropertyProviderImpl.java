package me.bvn13.lesson.camel.testing.cameltesting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class PropertyProviderImpl implements PropertiesProvider {

    @Value("${app.input.folder}")
    private String inputFolder;

    @Value("${app.output.folder}")
    private String outputFolder;

    @Override
    public String getInputEndpoint() {
        return "file:" + inputFolder;
    }

    @Override
    public String getOutputEndpoint() {
        return "file:" + outputFolder;
    }

    @Override
    public String getSupervisorEndpoint() {
        return "http://localhost:8080/supervisor?httpMethod=POST";
    }

}
