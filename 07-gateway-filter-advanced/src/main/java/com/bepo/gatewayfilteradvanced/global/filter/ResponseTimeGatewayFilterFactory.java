package com.bepo.gatewayfilteradvanced.global.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ResponseTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<ResponseTimeGatewayFilterFactory.Config> {

    private static final String START_TIME_ATTR = "rt-start";

    public ResponseTimeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Pre
            exchange.getAttributes().put(START_TIME_ATTR, System.currentTimeMillis());

            return chain.filter(exchange)
                    // Post
                    .then(Mono.fromRunnable(() -> {
                        Long start = exchange.getAttribute(START_TIME_ATTR);
                        long elapsed = System.currentTimeMillis() - (start != null ? start : 0L);

                        exchange.getResponse()
                                .getHeaders()
                                .add(config.getHeaderName(), elapsed + "ms");
                    }));
        };
    }

    @Getter
    @Setter
    public static class Config {
        private String headerName = "X-Response-Time";
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("headerName");
    }
}
