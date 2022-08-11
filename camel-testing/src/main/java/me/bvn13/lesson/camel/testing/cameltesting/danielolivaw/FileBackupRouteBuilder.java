package me.bvn13.lesson.camel.testing.cameltesting.danielolivaw;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FileBackupRouteBuilder extends RouteBuilder {

    public static final String SOURCE_FILE_NAME = "SourceFileName";
    public static final String DESTINATION_FILE_NAME = "DestinationFileName";
    public static final String DATE_PART_1 = "DATE_PART_1";
    public static final String DATE_PART_2 = "DATE_PART_2";
    public static final String DATE_PART_3 = "DATE_PART_3";

    @Value("${source.path}")
    private String sourcePath;
    @Value("${destination.path}")
    private String destinationPath;

    @Override
    public void configure() throws Exception {

        from("file://" + sourcePath + "?delete=false&recursively=true")
                .routeId("Transfer_Backup")
                .setProperty(SOURCE_FILE_NAME, header("CamelFileName"))
                .log("Transferring ${exchangeProperty["+SOURCE_FILE_NAME+"]}")
                .process(exchange -> {
                    String sourceFileName = exchange.getProperty(SOURCE_FILE_NAME, String.class);
                    // be sure sourceFileName is a name without path
                    String newFileName = sourceFileName + ".bup"; // maybe

                    LocalDate now = LocalDate.now();
                    exchange.setProperty(DATE_PART_1, now.getYear());
                    exchange.setProperty(DATE_PART_2, now.getMonthValue());
                    exchange.setProperty(DATE_PART_3, now.getDayOfMonth());
                    exchange.setProperty(DESTINATION_FILE_NAME, newFileName);
                })
                .toD("file://" + destinationPath +
                        "/${exchangeProperty["+DATE_PART_1+"]}" +
                        "/${exchangeProperty["+DATE_PART_2+"]}" +
                        "/${exchangeProperty["+DATE_PART_3+"]}" +
                        "/${exchangeProperty["+DESTINATION_FILE_NAME+"]}")
        ;

    }
}
