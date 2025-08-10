package com.ragukvn.data.manager.advise;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvise {

    @ModelAttribute
    public void logRequest(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        log.info("Received {} request for URI: {} with query: {}", method, uri, queryString != null ? queryString : "No Query String");
        // this will print the account number kind of information in the logs, to handle that we need to implement a masking strategy in logs configuration
    }

}
