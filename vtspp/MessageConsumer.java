package br.vtspp;

import java.util.function.Consumer;

public interface MessageConsumer<T extends Event> extends Consumer<T> {
    Event onError(Throwable throwable);
}
