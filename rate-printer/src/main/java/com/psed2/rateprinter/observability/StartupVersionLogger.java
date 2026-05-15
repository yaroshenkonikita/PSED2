package com.psed2.rateprinter.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StartupVersionLogger implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupVersionLogger.class);

    private final Environment environment;
    private final Optional<BuildProperties> buildProperties;

    public StartupVersionLogger(Environment environment, Optional<BuildProperties> buildProperties) {
        this.environment = environment;
        this.buildProperties = buildProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("Application {} started with version {}", applicationName(), applicationVersion());
    }

    private String applicationName() {
        return environment.getProperty("spring.application.name", "rate-printer");
    }

    private String applicationVersion() {
        return buildProperties.map(BuildProperties::getVersion).orElse("dev");
    }
}
