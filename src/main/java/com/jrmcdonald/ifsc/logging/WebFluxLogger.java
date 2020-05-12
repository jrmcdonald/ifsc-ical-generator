package com.jrmcdonald.ifsc.logging;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.jrmcdonald.ifsc.filter.WebFluxFilter.CONTEXT_MAP;

public class WebFluxLogger {
    public <T> Consumer<Signal<T>> logOnNext(Consumer<T> log) {
        return signal -> {
            if (signal.getType() != SignalType.ON_NEXT) {
                return;
            }

            Optional<Map<String, String>> maybeContextMap = signal.getContext().getOrEmpty(CONTEXT_MAP);

            if (maybeContextMap.isPresent()) {
                MDC.setContextMap(maybeContextMap.get());
                try {
                    log.accept(signal.get());
                } finally {
                    MDC.clear();
                }
            } else {
                log.accept(signal.get());
            }
        };
    }

    public <T> Consumer<Signal<T>> logOnFinally(Runnable log) {
        return signal -> {
            Optional<Map<String, String>> maybeContextMap = signal.getContext().getOrEmpty(CONTEXT_MAP);

            if (maybeContextMap.isPresent()) {
                MDC.setContextMap(maybeContextMap.get());
                try {
                    log.run();
                } finally {
                    MDC.clear();
                }
            } else {
                log.run();
            }
        };
    }

    public <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> log) {
        return signal -> {
            if (!signal.isOnError()) {
                return;
            }

            Optional<Map<String, String>> maybeContextMap = signal.getContext().getOrEmpty(CONTEXT_MAP);

            if (maybeContextMap.isPresent()) {
                MDC.setContextMap(maybeContextMap.get());
                try {
                    log.accept(signal.getThrowable());
                } finally {
                    MDC.clear();
                }
            } else {
                log.accept(signal.getThrowable());
            }
        };
    }
}
