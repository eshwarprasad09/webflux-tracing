package com.eshwarprasad.webfluxlogtracing.controller;

import com.eshwarprasad.webfluxlogtracing.model.Quote;
import com.eshwarprasad.webfluxlogtracing.service.QuotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author eshwarprasad
 * @website <a href="coming soon">Portfolio</a>
 */


@RestController
@RequiredArgsConstructor
public class QuotesController {
    private final QuotesService quotesService;

    @GetMapping(value = "/quote", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Quote>> getQuote(){
        return quotesService.getQuote()
                .map(ResponseEntity::ok);
    }
}
