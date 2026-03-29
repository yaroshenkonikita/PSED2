package com.psed2.rateprinter.config;

import com.psed2.rateprinter.observability.ClientHttpLoggingInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(
            RestTemplateBuilder restTemplateBuilder,
            ClientHttpLoggingInterceptor clientHttpLoggingInterceptor
    ) {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .additionalInterceptors(clientHttpLoggingInterceptor)
                .build();
    }
}
