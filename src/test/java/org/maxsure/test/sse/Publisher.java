package org.maxsure.test.sse;

import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Dat Huynh
 * @since 1.0
 */
public class Publisher {
    private final WebClient webClient;

    public Publisher(WebClient webClient) {
        this.webClient = Objects.requireNonNull(webClient, "webClient");
    }

    public Publisher(String baseURL) {
        webClient = WebClient.create(baseURL);
    }

    public String publish(String routingKey, String body) {
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter =
                BodyInserters.fromPublisher(Mono.just(body), String.class);
        String publishURI = "/rest/v1.0/publish?routing-key=" + routingKey;
        String response = webClient.put()
                .uri(publishURI)
                .body(bodyInserter)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response.trim();
    }

}
