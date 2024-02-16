package fr.java.concurrency.service;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.tomakehurst.wiremock.WireMockServer;

import fr.java.concurrency.model.Dilly;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.util.Objects.isNull;
import static org.awaitility.Awaitility.await;

/**
 * @author gfourny
 */
class IntegrationTest {

    static WireMockServer wireMockServer = new WireMockServer(options().port(8081));

    @BeforeAll
    static void init() {
        wireMockServer.start();
    }
    
    @AfterAll
    static void stopWiremock(){
        wireMockServer.stop();
    }

    private static Stream<Arguments> classes() {
        return Stream.of(
                Arguments.of(new Blocking(), "Appel bloquant"),
                Arguments.of(new Threads(), "Thread"),
                Arguments.of(new ThreadPool(), "ThreadPool"),
                Arguments.of(new Futures(), "Future"),
                Arguments.of(new CompletableFutureApi(), "CompletableFuture"),
                Arguments.of(new CompletableFutureWithVT(), "CompletableFuture avec Virtual Thread"),
                Arguments.of(new VirtualThread(), "Virtual Thread"),
                Arguments.of(new StructuredConcurrency(), "StructuredConcurrency")
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("classes")
    void drink(Caller caller, String name) {

        await().atMost(Duration.ofMillis(1700)).until(() ->
                {
                    Dilly dilly = caller.async();
                    return !isNull(dilly) && !isNull(dilly.beer()) && !isNull(dilly.vodka());
                }
        );
    }
}
