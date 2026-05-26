package com.bepo.gateway.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/routes")
@RequiredArgsConstructor
public class DynamicRouteController {

    private final RouteDefinitionWriter routeDefinitionWriter;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping
    public Mono<ResponseEntity<String>> add(
            @RequestBody
            RouteDefinition definition
    ) {
        return routeDefinitionWriter
                .save(Mono.just(definition))
                .then(Mono.fromRunnable(this::publishRefresh))
                .then(Mono.just(ResponseEntity.ok("추가됨: " + definition.getId())));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(
            @PathVariable
            String id
    ) {
        return routeDefinitionWriter
                .delete(Mono.just(id))
                .then(Mono.fromRunnable(this::publishRefresh))
                .then(Mono.just(ResponseEntity.ok("삭제됨: " + id)))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    private void publishRefresh() {
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
