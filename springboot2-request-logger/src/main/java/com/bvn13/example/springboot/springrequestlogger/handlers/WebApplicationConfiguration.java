package com.bvn13.example.springboot.springrequestlogger.handlers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author bvn13
 * @since 30.09.2019
 */
@Configuration
public class WebApplicationConfiguration implements WebMvcConfigurer {

    @Setter(onMethod_ = @Autowired)
    private RequestLoggingHandler requestLogger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogger);
    }
}
