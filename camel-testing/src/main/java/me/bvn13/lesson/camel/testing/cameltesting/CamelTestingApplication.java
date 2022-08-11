package me.bvn13.lesson.camel.testing.cameltesting;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class CamelTestingApplication {

    public static void main(String[] args) {
//        SpringApplication.run(CamelTestingApplication.class, args);
        new SpringApplicationBuilder(CamelTestingApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
