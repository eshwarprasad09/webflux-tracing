package com.eshwarprasad.webfluxlogtracing.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author eshwarprasad
 * @website <a href="coming soon">Portfolio</a>
 */

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.security.jwt.enabled", havingValue = "true")
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer "))
                .flatMap(header -> {
                    String authToken = header.substring(7);
                    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authToken, null);
                    return authenticationManager.authenticate(authenticationToken)
                            .map(SecurityContextImpl::new).doOnError(error -> log.error("error while authenticating user {}",error.getMessage()));
                });
    }
}
