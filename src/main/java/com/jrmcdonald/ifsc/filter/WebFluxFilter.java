package com.jrmcdonald.ifsc.filter;

import com.jrmcdonald.ifsc.logging.WebFluxLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.nanoTime;

@Slf4j
@RequiredArgsConstructor
public class WebFluxFilter implements WebFilter {

    public static final String LOG_CONTEXT_MAP = "log-context";
    public static final String APPLICATION_KEY = "application";
    private static final BigDecimal ONE_MILLION = BigDecimal.valueOf(1E6);
    private static final String DURATION = "duration";
    private static final String HTTP_STATUS_CODE = "httpStatusCode";
    private static final String REQUEST_START_TIME = "requestStartTime";
    private final String applicationName;
    private final WebFluxLogger webFluxLogger;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> contextMap = generateReactorContextMap();

        exchange.getAttributes().put(REQUEST_START_TIME, nanoTime());

        return chain.filter(exchange)
                .doFirst(() -> {
                    MDC.setContextMap(contextMap);
                    log.info("Entering service");
                })
                .doOnEach(webFluxLogger.logOnFinally(() -> {
                    addDurationAndStatusToMDC(exchange, exchange.getResponse());
                    log.info("Exiting service");
                }))
                .subscriberContext(context -> context.put(LOG_CONTEXT_MAP, contextMap));
    }

    private Map<String, String> generateReactorContextMap() {
        Map<String, String> map = new HashMap<>();

        map.put(APPLICATION_KEY, applicationName);

        return map;
    }

    private void addDurationAndStatusToMDC(ServerWebExchange request, ServerHttpResponse response) {
        String timeInNanos = calculateExecutionTime(request);
        if (timeInNanos != null) {
            MDC.put(DURATION, timeInNanos);
        }
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode != null) {
            MDC.put(HTTP_STATUS_CODE, String.valueOf(statusCode.value()));
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
