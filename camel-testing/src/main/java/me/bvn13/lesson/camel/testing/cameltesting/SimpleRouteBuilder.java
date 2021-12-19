package me.bvn13.lesson.camel.testing.cameltesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static me.bvn13.lesson.camel.testing.cameltesting.SupervisorResponseDto.Verdict.SKIP;

@Converter
@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleRouteBuilder extends RouteBuilder {

    @Value("${app.bad-word}")
    private String badWord;

    @Value("${app.good-word}")
    private String goodWord;

    private final PropertiesProvider propertiesProvider;

    public static final String SUPERVISION_ROUTE_ID = "SUPERVISOR";
    public static final String EXTERNAL_SUPERVISION_ROUTE_ID = "EXTERNAL-SUPERVISOR";
    public static final String SUPERVISION_VERDICT = "VERDICT";

    @Override
    public void configure() throws Exception {

        final String source = propertiesProvider.getInputEndpoint();
        final String destination = propertiesProvider.getOutputEndpoint();
        log.info("starting processing from {} to {}", source, destination);

        getCamelContext().setTracing(true);
        getCamelContext().setTypeConverterStatisticsEnabled(true);

        from(source)                                                              // listen for new files
                .tracing()
                .log("processing ${header[CamelFileName]}")
                .to("direct://supervisor")
                .choice()
                    .when(exchangeProperty(SUPERVISION_VERDICT).isEqualTo(SKIP))
                        .to("direct://skip")
                    .otherwise()
                        .to("direct://process");

        from("direct://supervisor")
                .routeId(SUPERVISION_ROUTE_ID)                                    // route ID is customized
                .setHeader("file-name", header("CamelFileName"))
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON_VALUE))
                .enrich("direct://external-supervisor", (oldExchange, newExchange) -> { // check the file with supervision
                    oldExchange.setProperty(SUPERVISION_VERDICT, newExchange.getIn().getBody(SupervisorResponseDto.class).getVerdict());
                    return oldExchange;
                });

        from("direct://external-supervisor")
                .routeId(EXTERNAL_SUPERVISION_ROUTE_ID)                                    // route ID is customized
                .setHeader("file-name", header("CamelFileName"))
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON_VALUE))
                .to(propertiesProvider.getSupervisorEndpoint())
                .convertBodyTo(String.class)
                .process(exchange -> {
                    exchange.getIn().getBody(String.class);
                })
                .unmarshal().json(SupervisorResponseDto.class);


        from("direct://skip")
                .log("Skipping file ${header[CamelFileName]}")
                .end();

        from("direct://process")
                .log("Processing file ${header[CamelFileName]}")
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

    @Converter
    public SupervisorResponseDto convert(InputStream stream, Exchange exchange) throws Exception {
        return new ObjectMapper().convertValue(stream.readAllBytes(), SupervisorResponseDto.class);
    }

}
