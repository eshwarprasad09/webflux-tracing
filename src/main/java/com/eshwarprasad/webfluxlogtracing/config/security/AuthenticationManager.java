package com.eshwarprasad.webfluxlogtracing.config.security;

import com.eshwarprasad.webfluxlogtracing.service.UserService;
import com.eshwarprasad.webfluxlogtracing.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author eshwarprasad
 * @website <a href="coming soon">Portfolio</a>
 */

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.security.jwt.enabled", havingValue = "true")
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;
    private final UserService service;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getPrincipal().toString();
        String username = jwtUtil.extractUsername(authToken);
        return service.findUserByEmail(username).doOnNext(res -> log.info("found username {}",res.getUsername())).doOnError(
                error -> log.info("error while finding username {}",error.getMessage())
                )
                .filter(userDetails -> jwtUtil.validateToken(authToken, userDetails))
                .switchIfEmpty(Mono.empty())
                .map(userDetails -> {
//                    Claims claims = jwtUtil.extractClaim(authToken, c -> c);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            userDetails.getAuthorities()
                    );
                });
    }
}
