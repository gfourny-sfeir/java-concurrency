package fr.java.concurrency.service;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import fr.java.concurrency.model.Dilly;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.util.Objects.isNull;
import static org.awaitility.Awaitility.await;

/**
 * @author gfourny
 */
class ThreadsTest {

    private final Threads threads = new Threads();

    private final WireMockServer wireMockServer = new WireMockServer(options().port(8081)); //No-args constructor will start on port 8080, no HTTPS

    @BeforeEach
    void init() {
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void drink() {

        await().atMost(Duration.ofMillis(1700)).until(() ->
                {
                    Dilly dilly = threads.async();
                    return !isNull(dilly) && !isNull(dilly.beer()) && !isNull(dilly.vodka());
                }
        );
    }
}