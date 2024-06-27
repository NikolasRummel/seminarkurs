package net.venade.services.cloud.gateway.config;

import net.venade.services.cloud.gateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nikolas Rummel
 * @since 14.12.21
 */
@Configuration
public class RouteConfiguration {

    @Autowired
    AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .path("/user/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://localhost:8082")
                        .id("userService"))
                .route(r -> r
                        .path("/minecraft/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://localhost:8083")
                        .id("minecraftService"))
                .build();
    }
}
