package br.vtspp;

import br.vtspp.ErrorEvent;
import br.vtspp.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Configuration
public class MessageConfiguration {

    @Bean
    public ForkJoinPool forkJoinPool(final ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>> messageBroker) {
        final var numberAvailableProcessors = Runtime.getRuntime().availableProcessors() - 1;
        final var asyncMode = true;
        return new ForkJoinPool(300 / numberAvailableProcessors, ForkJoinPool.defaultForkJoinWorkerThreadFactory, (t, e) -> messageBroker.get("errorEvent").offer(new ErrorEvent(e).serialize()), asyncMode);
    }

    @Bean
    public ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>> messageBroker() {
        final var messageBroker = new ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>>();
        messageBroker.put("errorEvent", new ConcurrentLinkedQueue<>());
        messageBroker.put("createThirdPartyEvent", new ConcurrentLinkedQueue<>());
        messageBroker.put("createPolicyEvent", new ConcurrentLinkedQueue<>());
        return messageBroker;
    }

    @Bean
    public BiConsumer<String, Event> producer(final ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>> messageBroker) {
        return (topic, event) -> messageBroker.get(topic).offer(event.serialize());
    }

    @Bean
    public Function<String, Event> consumer(final ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>> messageBroker) {
        return (topic) -> Event.deserialize(messageBroker.get(topic).remove());
    }
}
