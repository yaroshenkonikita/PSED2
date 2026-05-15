package com.psed2.currencyrateprovider.contract;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@Provider("currency-rate-provider")
@PactBroker(
        host = "${pactbroker.host:localhost}",
        port = "${pactbroker.port:9292}",
        scheme = "${pactbroker.scheme:http}"
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RateProviderPactVerificationTest {

    @LocalServerPort
    int port;

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void verifyPacts(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @State("rate for pair and time exists")
    void rateForPairAndTimeExists() {
        // provider always can calculate known pair rates for given timestamps
    }
}
