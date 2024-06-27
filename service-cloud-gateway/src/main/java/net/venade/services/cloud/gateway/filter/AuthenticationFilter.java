package net.venade.services.cloud.gateway.filter;

import net.venade.starters.auth.AuthProvider;
import net.venade.starters.models.ServiceRegistry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Nikolas Rummel
 * @since 14.12.21
 */

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

   private AuthProvider authProvider = ServiceRegistry.getProvider(AuthProvider.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //If user tries to login or register -> no token needed
        if(request.getPath().toString().startsWith("/user/login") || request.getPath().toString().startsWith(
            "/user/register")) return chain.filter(exchange);

        //No Auth
        System.out.println(request.getHeaders());
        if(request.getHeaders().get("Authorization") == null) {
            System.out.println("New Request without token. returning...");
            return Mono.empty();
        }

        String bearerToken = request.getHeaders().get("Authorization").get(0);
        System.out.println("New Request with token: " + bearerToken);

        boolean tokenCorrect = authProvider.checkToken(bearerToken);

        // Token Incorrect
        if(!tokenCorrect) {
            System.out.println("Token " + bearerToken + " was not correct.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

            // Token correct
        } else return chain.filter(exchange); // Forward to route
    }
}