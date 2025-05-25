package br.vtspp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorEventConsumer implements MessageConsumer<ErrorEvent> {

    @Override
    public void accept(ErrorEvent errorEvent) {
        log.error("{} - Process error event {}", Thread.currentThread().getName(), errorEvent);

    }

    @Override
    public Event onError(Throwable throwable) {
        return new ErrorEvent(throwable);
    }
}
