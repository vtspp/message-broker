package br.vtspp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
class WorkersFactory implements CommandLineRunner {

    private static final String consumerSuffix = "Consumer";

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>> brokerMessages;
    private final ForkJoinPool forkJoinPool;
    private final MessageBrokerProcessor messageBrokerProcessor;
    private final ApplicationContext applicationContext;
    private final AtomicInteger workersCreated;
    private final int maxWorkersQuantity;
    private final int topicSize;

    public WorkersFactory(ConcurrentHashMap<String, ConcurrentLinkedQueue<byte[]>> brokerMessages, ForkJoinPool forkJoinPool, MessageBrokerProcessor messageBrokerProcessor, ApplicationContext applicationContext) {
        this.brokerMessages = brokerMessages;
        this.forkJoinPool = forkJoinPool;
        this.messageBrokerProcessor = messageBrokerProcessor;
        this.applicationContext = applicationContext;
        this.workersCreated = new AtomicInteger(0);
        this.maxWorkersQuantity = forkJoinPool.getParallelism();
        this.topicSize = brokerMessages.size();
    }

    @Override
    public void run(String... args) {
        var topics = Collections.list(this.brokerMessages.keys());
        var topicPosition =  new AtomicInteger(0);

        while (hasNextTopic(topicPosition) && workersCreated.get() < maxWorkersQuantity) {
            createWorker(topics, topicPosition);
            updateTopicPosition(topicPosition);
        }
        log.info("Created {} Workers with success", workersCreated.get());
    }

    private void createWorker(List<String> topics, final AtomicInteger positionTopic) {
        var topic = topics.get(positionTopic.get());
        var messageConsumer = (MessageConsumer<Event>) applicationContext.getBean(topic + consumerSuffix);
        forkJoinPool.submit(() -> messageBrokerProcessor.eventLoop(topic, messageConsumer));
        workersCreated.getAndIncrement();
    }

    private void updateToInitialPosition(final AtomicInteger topicPosition) {
        final var initialPosition = 0;
        topicPosition.set(initialPosition);
    }

    private void updateTopicPosition(final AtomicInteger topicPosition) {
        topicPosition.getAndIncrement();
    }

    private boolean hasNextTopic(final AtomicInteger created) {
        if (created.get() >= topicSize) {
            updateToInitialPosition(created);
        }
        return created.get() < topicSize;
    }
}
