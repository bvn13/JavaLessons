package com.bvn13.example.time.duration.timeduration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimedurationApplicationTests {

    @Test
    public void testTimeDurationRepresentation() {

        LocalDateTime start = LocalDateTime.of(2019, 1, 1, 12, 30, 00);
        LocalDateTime end = LocalDateTime.now();

        LocalDateTime temp = LocalDateTime.from(start);

        long years = temp.until( end, ChronoUnit.YEARS);
        temp = temp.plusYears( years );

        long months = temp.until( end, ChronoUnit.MONTHS);
        temp = temp.plusMonths( months );

        long days = temp.until( end, ChronoUnit.DAYS);
        temp = temp.plusDays( days );


        long hours = temp.until( end, ChronoUnit.HOURS);
        temp = temp.plusHours( hours );

        long minutes = temp.until( end, ChronoUnit.MINUTES);
        temp = temp.plusMinutes( minutes );

        long seconds = temp.until( end, ChronoUnit.SECONDS);

        System.out.println(String.format("%d years %d months %d days %d hours %d minutes %d seconds", years, months, days, hours, minutes, seconds));

    }

    @Test
    public void testTimeDurationRepresentationInDays() {

        LocalDateTime start = LocalDateTime.of(2019, 1, 1, 12, 30, 00);
        LocalDateTime end = LocalDateTime.now();

        LocalDateTime temp = LocalDateTime.from(start);

//        long years = temp.until( end, ChronoUnit.YEARS);
//        temp = temp.plusYears( years );
//
//        long months = temp.until( end, ChronoUnit.MONTHS);
//        temp = temp.plusMonths( months );
//
        long days = temp.until( end, ChronoUnit.DAYS);
        temp = temp.plusDays( days );


        long hours = temp.until( end, ChronoUnit.HOURS);
        temp = temp.plusHours( hours );

        long minutes = temp.until( end, ChronoUnit.MINUTES);
        temp = temp.plusMinutes( minutes );

        long seconds = temp.until( end, ChronoUnit.SECONDS);

        System.out.println(String.format("%d days %d hours %d minutes %d seconds", days, hours, minutes, seconds));

    }

}
