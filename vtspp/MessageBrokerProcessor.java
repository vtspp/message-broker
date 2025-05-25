package br.vtspp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
class MessageBrokerProcessor {

    private final Function<String, Event> consumer;
    private final BiConsumer<String, Event> producer;

    protected void eventLoop(final String topicName, final MessageConsumer<Event> messageConsumer) {
        while (true) {
            try {
                var event = consumer.apply(topicName);
                messageConsumer.accept(event);
            } catch (Exception e1) {
                if (e1 instanceof NoSuchElementException) {
                    try {
                        Thread.sleep(2000);
                        continue;
                    } catch (InterruptedException e2) {
                        log.error(e2.getMessage());
                    }
                }
                producer.accept("errorEvent", messageConsumer.onError(e1));
            }
        }
    }
}
