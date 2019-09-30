package com.bvn13.example.springboot.springrequestlogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringrequestloggerApplicationTests {

    @LocalServerPort
    int port;

    private RestTemplate restTemplate;

    @Before
    public void init() {
        restTemplate = new RestTemplate();
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void testControllerLoggingWithFilter() {
        restTemplate.getForObject("http://localhost:"+port+"/", String.class);
        restTemplate.getForObject("http://localhost:"+port+"/test.js", String.class);
        restTemplate.getForObject("http://localhost:"+port+"/test.css", String.class);
        restTemplate.getForObject("http://localhost:"+port+"/does-not-exist.file", String.class);
    }

}
