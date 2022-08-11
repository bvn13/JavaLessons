package me.bvn13.lesson.camel.testing.cameltesting;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration()
                .port(8085)
//                .host("localhost")
        ;

        rest()
                .get("/test")
                .route()
                .log("${body}")
                .setBody().body(ex -> "<html><body>OK</body></html>")
                .end()
                .endRest()
        ;

    }
}
