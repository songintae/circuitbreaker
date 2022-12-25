package com.example.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return (factory) -> {
            factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .build());
        };
    }

    /**
     * 참고
     * CircuitBreaker 객체가 생성됬으나 Caller에게 reture되기 이전에도 설정을 변경할 수 있다.
     * addCircuitBreakerCustomizer를 통해 변경할 수 있고, 일반적으로 event hadnler를 등록할 때 유용한다
     * https://docs.spring.io/spring-cloud-circuitbreaker/docs/current/reference/html/
     * @return
     */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> slowCustomizer() {
        return (factory) -> {
            factory.configure(builder -> builder.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .build(), "slow");
        };
    }


}
