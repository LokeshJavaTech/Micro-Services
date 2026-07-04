package com.lokesh.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder)
	{
		return builder.routes()
				.route(p -> p.path("/eazybank/accounts-ms/**")
						.filters(
								f -> f.rewritePath("/eazybank/accounts-ms/(?<segment>.*)", "/${segment}")
													  .circuitBreaker(config -> config.setName("accountsCircuitBreaker")
															  								 .setFallbackUri("forward:/contactSupport")
													  )
						)
						.uri("lb://ACCOUNTS-MS"))
				.route(p -> p.path("/eazybank/loans-ms/**")
						.filters(f -> f.rewritePath("/eazybank/loans-ms/(?<segment>.*)", "/${segment}"))
						.uri("lb://LOANS-MS"))
				.route(p -> p.path("/eazybank/cards-ms/**")
						.filters(f -> f.rewritePath("/eazybank/cards-ms/(?<segment>.*)", "/${segment}"))
						.uri("lb://CARDS-MS"))
				.build();
	}
}
