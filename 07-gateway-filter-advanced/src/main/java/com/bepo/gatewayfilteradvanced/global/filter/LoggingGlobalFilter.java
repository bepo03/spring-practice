package com.bepo.gatewayfilteradvanced.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String START_TIME_ATTR = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = exchange.getRequest()
                .getHeaders()
                .getFirst(TRACE_ID_HEADER);

        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }

        final String finalTraceId = traceId;
        ServerWebExchange mutated = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header(TRACE_ID_HEADER, finalTraceId)
                        .build())
                .build();

        long startTime = System.currentTimeMillis();
        mutated.getAttributes().put(START_TIME_ATTR, startTime);

        log.info("[REQ] traceId={} method={} path={}",
                finalTraceId,
                mutated.getRequest().getMethod(),
                mutated.getRequest().getURI().getPath()
        );

        return chain.filter(mutated)
                .doFinally(signal -> {
                    Long start = mutated.getAttribute(START_TIME_ATTR);
                    long elapsed = System.currentTimeMillis() - (start != null ? start : startTime);
                    int status = mutated.getResponse().getStatusCode() != null
                            ? mutated.getResponse().getStatusCode().value()
                            : 0;

                    log.info("[RES] traceId={} status={} elapsed={}ms signal={}",
                            finalTraceId,
                            status,
                            elapsed,
                            signal
                    );
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
