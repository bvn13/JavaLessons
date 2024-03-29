package me.bvn13.lesson.camel.testing.cameltesting;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static me.bvn13.lesson.camel.testing.cameltesting.SimpleRouteBuilder.SUPERVISION_ROUTE_ID;
import static me.bvn13.lesson.camel.testing.cameltesting.SupervisorResponseDto.Verdict.*;

@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = CamelTestingApplication.class, properties = {
        "app.bad-word=duck",
        "app.good-word=ostrich",
        "app.input.folder=/tmp/in",
        "app.output.folder=/tmp/out"
})
@CamelSpringBootTest
@UseAdviceWith
class CamelTestingApplicationTests {

    private static final String BAD = "The duck flies away " + Strings.LINE_SEPARATOR + " to the south";
    private static final String GOOD = "The ostrich flies away " + Strings.LINE_SEPARATOR + " to the south";

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    PropertiesProvider propertiesProvider;

    @Autowired
    CamelContext camelContext;

    @DirtiesContext
    @Test
    void givenRoute_whenFileAppears_thenAllBadWordsChanged() throws Exception {

        // given
        MockEndpoint endpoint = camelContext.getEndpoint(propertiesProvider.getOutputEndpoint(), MockEndpoint.class);
        endpoint.expectedBodiesReceived(GOOD);
        endpoint.setExpectedCount(1);
        mockSupervision(PROCESS);

        camelContext.start(); // start CamelContext explicitly

        // when
        fileAppears();

        // then
        endpoint.assertIsSatisfied();

        camelContext.stop(); // stop CamelContext explicitly

    }

    @DirtiesContext
    @Test
    void givenRoute_whenFileAppearsAndRejectedBySupervisor_thenFileIsSkipped() throws Exception {

        // given
        MockEndpoint endpoint = camelContext.getEndpoint(propertiesProvider.getOutputEndpoint(), MockEndpoint.class);
        endpoint.setExpectedCount(0);
        mockSupervision(SKIP);

        camelContext.start(); // start CamelContext explicitly

        // when
        fileAppears();

        // then
        endpoint.assertIsSatisfied();

        camelContext.stop(); // stop CamelContext explicitly

    }

    void mockSupervision(SupervisorResponseDto.Verdict verdict) throws Exception {
        AdviceWith.adviceWith(camelContext, SUPERVISION_ROUTE_ID, in -> in
                .interceptSendToEndpoint(propertiesProvider.getSupervisorEndpoint())
                .setBody(exchange -> new SupervisorResponseDto(verdict)));
    }

    void fileAppears() {
        producerTemplate.asyncSend(propertiesProvider.getInputEndpoint(), exchange -> {
            exchange.getIn().setBody(BAD);
            exchange.getIn().setHeader("CamelFileName", "test-1");
        });
    }

}

