package me.bvn13.lesson.camel.testing.cameltesting;

public interface PropertiesProvider {

    String getInputEndpoint();
    String getOutputEndpoint();
    String getSupervisorEndpoint();

}
