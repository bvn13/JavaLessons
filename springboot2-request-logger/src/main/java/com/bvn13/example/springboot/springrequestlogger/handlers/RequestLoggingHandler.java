package com.bvn13.example.springboot.springrequestlogger.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bvn13
 * @since 30.09.2019
 */
@Slf4j
@Component
public class RequestLoggingHandler extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug(
                String.format("HANDLER(pre) URL: %s", request.getRequestURI())
        );

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug(
                String.format("HANDLER(post) URL: %s", request.getRequestURI())
        );

        super.postHandle(request, response, handler, modelAndView);
    }
}
