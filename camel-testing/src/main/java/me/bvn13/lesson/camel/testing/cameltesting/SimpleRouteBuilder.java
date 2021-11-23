package me.bvn13.lesson.camel.testing.cameltesting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleRouteBuilder extends RouteBuilder {

    @Value("${app.bad-word}")
    private String badWord;

    @Value("${app.good-word}")
    private String goodWord;

    private final PropertiesProvider propertiesProvider;

    public static final String SUPERVISION_ROUTE_ID = "START_ROUTE";
    public static final String SUPERVISION_VERDICT = "VERDICT";
    public static final String SUPERVISION_VERDICT_SKIP = "SKIP";

    @Override
    public void configure() throws Exception {

        final String source = propertiesProvider.getInputEndpoint();
        final String destination = propertiesProvider.getOutputEndpoint();
        log.info("starting processing from {} to {}", source, destination);

        from(source)                                                              // listen for new files
                .tracing()
                .log("processing ${header[CamelFileName]}")
                .to("direct://supervisor")
                .choice()
                    .when(exchangeProperty(SUPERVISION_VERDICT).isEqualTo(SUPERVISION_VERDICT_SKIP))
                        .to("direct://skip")
                    .otherwise()
                        .to("direct://process");

        from("direct://supervisor")
                .routeId(SUPERVISION_ROUTE_ID)                                    // route ID is customized
                .enrich(propertiesProvider.getSupervisorEndpoint(), (oldExchange, newExchange) -> { // check the file with supervision
                    oldExchange.setProperty(SUPERVISION_VERDICT, newExchange.getIn().getBody(String.class));
                    return oldExchange;
                });
        
        from("direct://skip")
                .log("Skipping file ${header[CamelFileName]}")
                .end();

        from("direct://process")
                .split().body()                                                   // split every file line-by-line
                .bean(this, "replaceBadWordWithPermitted")          // remove bad word
                .aggregate(header("CamelFileName"), AggregationStrategies  // aggregate all lines into ArrayList
                        .flexible(String.class)
                        .accumulateInCollection(ArrayList.class)
                        .pick(body()))
                .completion(header("CamelSplitSize"))                      // don't mix different files
                .bean(this, "prepareForWrite")                      // literally convert ArrayList<String> into byte[]
                .to(destination);                                                 // write the file out

    }

    @Handler
    public String replaceBadWordWithPermitted(@Body String row) {
        return row.replaceAll(badWord, goodWord);
    }

    @Handler
    public byte[] prepareForWrite(@Body List<String> rows) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String row : rows) {
            if (i > 0) {
                sb.append(Strings.LINE_SEPARATOR);
            }
            i++;
            sb.append(row);
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}
