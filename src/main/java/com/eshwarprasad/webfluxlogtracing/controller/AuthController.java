package com.eshwarprasad.webfluxlogtracing.controller;

import com.eshwarprasad.webfluxlogtracing.model.AuthReq;
import com.eshwarprasad.webfluxlogtracing.model.AuthRes;
import com.eshwarprasad.webfluxlogtracing.util.JwtUtil;
import com.eshwarprasad.webfluxlogtracing.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author eshwarprasad
 * @website <a href="coming soon">Portfolio</a>
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthRes>> login(@RequestBody AuthReq authReq){
        return userService.findUserByEmail(authReq.username()).doOnNext(res -> log.info("found username {}",res.getUsername()))
                .map(userDetails -> ResponseEntity.ok(new AuthRes(jwtUtil.generateToken(userDetails))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())).doOnNext(authResResponseEntity -> {
                    if(authResResponseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED)
                    log.info("no user found");
                    else log.info("user found");
                });
    }
}
