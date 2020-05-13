package com.jrmcdonald.ifsc.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.nanoTime;

@Slf4j
@RequiredArgsConstructor
public class WebFluxContextFilter implements WebFilter {

    public static final String LOG_CONTEXT_KEY = "log-context";

    private static final BigDecimal ONE_MILLION = BigDecimal.valueOf(1E6);
    private static final String REQUEST_START_TIME = "requestStartTime";

    public static final String APPLICATION_KEY = "application";
    public static final String DURATION_KEY = "duration";
    public static final String HTTP_METHOD_KEY = "httpMethod";
    public static final String HTTP_STATUS_CODE_KEY = "httpStatus";
    public static final String URI_KEY = "uri";

    private final String applicationName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> reactorContext = buildReactorContext(exchange.getRequest());

        exchange.getAttributes().put(REQUEST_START_TIME, nanoTime());

        return chain.filter(exchange)
                .doFirst(() -> {
                    MDC.setContextMap(reactorContext);
                    log.info("Entering service");
                    MDC.clear();
                })
                .doFinally(signal -> {
//                    MDC.setContextMap(reactorContext);
                    addDurationAndStatusToMDC(exchange);
                    log.info("Exiting service");
                })
                .subscriberContext(Context.of(reactorContext));
    }

    private Map<String, String> buildReactorContext(ServerHttpRequest request) {
        Map<String, String> initialContext = new HashMap<>();

        initialContext.put(APPLICATION_KEY, applicationName);
        initialContext.put(HTTP_METHOD_KEY, request.getMethodValue());
        initialContext.put(URI_KEY, request.getURI().toString());

        return initialContext;
    }

    private void addDurationAndStatusToMDC(ServerWebExchange exchange) {
        String timeInNanos = calculateExecutionTime(exchange);
        if (timeInNanos != null) {
            MDC.put(DURATION_KEY, timeInNanos);
        }
        HttpStatus statusCode = exchange.getResponse().getStatusCode();
        if (statusCode != null) {
            MDC.put(HTTP_STATUS_CODE_KEY, String.valueOf(statusCode.value()));
        }
    }

    private String calculateExecutionTime(ServerWebExchange request) {
        Long requestStartTime = request.getAttribute(REQUEST_START_TIME);
        if (requestStartTime == null) {
            return null;
        }
        request.getAttributes().remove(REQUEST_START_TIME);
        long requestEndTime = nanoTime();
        return BigDecimal.valueOf(requestEndTime)
                .subtract(BigDecimal.valueOf(requestStartTime))
                .divide(ONE_MILLION, 0, RoundingMode.UP)
                .toString();
    }
}
