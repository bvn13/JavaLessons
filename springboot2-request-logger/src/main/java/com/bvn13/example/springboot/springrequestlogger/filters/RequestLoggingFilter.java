package com.bvn13.example.springboot.springrequestlogger.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bvn13
 * @since 29.09.2019
 */
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug(
                String.format("FILTERED URL: %s", request.getRequestURI())
        );

        //continue filtering
        filterChain.doFilter(request, response);
    }
}
