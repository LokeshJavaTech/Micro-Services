package com.lokesh.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity)
    {
        serverHttpSecurity
                .authorizeExchange(
                        exchange -> exchange
                                .pathMatchers(HttpMethod.GET).permitAll()
                                .pathMatchers("/eazybank/accounts-ms/**").authenticated()
                                .pathMatchers("/eazybank/loans-ms/**").authenticated()
                                .pathMatchers("/eazybank/cards-ms/**").authenticated()
                )
                .oauth2ResourceServer(serverSpec -> serverSpec
                        .jwt(Customizer.withDefaults())
                );

        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

        return serverHttpSecurity.build();
    }

}
