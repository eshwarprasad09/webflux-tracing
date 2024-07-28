package com.eshwarprasad.webfluxlogtracing.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

/**
 * @author eshwarprasad
 * @website <a href="coming soon">Portfolio</a>
 */


@Configuration(proxyBeanMethods = false)
public class TracerConfiguration {
    /** This filter bean will add traceId in response headers
     *
     * @param tracer
     * @return WebFilter
     */
    @Bean
    WebFilter traceIdInResponseFilter(Tracer tracer) {
        //            @Observed
        return (exchange, chain) -> {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                // putting trace id value in [traceId] response header
                exchange.getResponse().getHeaders().add("traceId", currentSpan.context().traceId());
                exchange.getResponse().getHeaders().add("spanId",currentSpan.context().spanId());
                exchange.getResponse().getHeaders().add("requestId",exchange.getLogPrefix());
            }
            return chain.filter(exchange);
        };
    }
}
