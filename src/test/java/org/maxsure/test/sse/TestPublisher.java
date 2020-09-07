package org.maxsure.test.sse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.maxsure.test.common.Utils;

/**
 *
 * @author Dat Huynh
 * @since 1.0
 */
class TestPublisher {
    private static Publisher publisher;

    @BeforeAll
    static void setup() {
        publisher = new Publisher("http://localhost:8080");
    }

    @Test
    void testPublish1() {
        String routingKey = Utils.uuid();
        String want = String.format("Published to: [%s]", routingKey);

        String body = Utils.uuid();
        String got = publisher.publish(routingKey, body);

        Assertions.assertEquals(want, got);
    }

}
