package com.psed2.rateprinter.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ClientHttpLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHttpLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
            org.springframework.http.HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(request, response);
        return response;
    }

    private void logRequest(org.springframework.http.HttpRequest request, byte[] body) {
        LOGGER.info(
                "Client request {} {} headers={} body={}",
                request.getMethod(),
                request.getURI(),
                request.getHeaders(),
                bodyAsText(body)
        );
    }

    private void logResponse(org.springframework.http.HttpRequest request, ClientHttpResponse response) throws IOException {
        LOGGER.info(
                "Client response {} {} status={} headers={} body={}",
                request.getMethod(),
                request.getURI(),
                response.getStatusCode(),
                response.getHeaders(),
                responseBody(response)
        );
    }

    private String bodyAsText(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }

    private String responseBody(ClientHttpResponse response) throws IOException {
        return StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
    }
}
