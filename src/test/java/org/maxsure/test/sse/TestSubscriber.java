package org.maxsure.test.sse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.maxsure.test.common.Utils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author Dat Huynh
 * @since 1.0
 */
@Slf4j
class TestSubscriber {

    private static WebClient webClient;
    private static Publisher publisher;

    @BeforeAll
    static void setup() {
        String baseURL = "http://localhost:8080";
        webClient = WebClient.create(baseURL);

        publisher = new Publisher(webClient);
    }

    @Test
    void testSubscribe1() throws InterruptedException, ExecutionException {
        String routingKey = Utils.uuid();
        String subscribeURI = "rest/v1.0/subscribe?routing-key=" + routingKey;

        String want = Utils.uuid();

        ParameterizedTypeReference<ServerSentEvent<String>> elementType =
                new ParameterizedTypeReference<ServerSentEvent<String>>() {};
        Flux<ServerSentEvent<String>> elementStream = webClient.get()
                .uri(subscribeURI)
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToFlux(elementType);

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        elementStream.subscribe(
                element -> {
                    completableFuture.complete(element.data());
                },
                error -> log.error("Error when processing messages", error),
                () -> log.info("Processing completed"));
        Thread.sleep(1000);

        publisher.publish(routingKey, want);
        String got = completableFuture.get();
        Assertions.assertEquals(want, got);
    }


}
