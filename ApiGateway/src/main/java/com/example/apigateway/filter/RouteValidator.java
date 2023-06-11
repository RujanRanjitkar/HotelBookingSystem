package com.example.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/users/add_new_user",
            "/api/login/authenticate",
            "/api/hotels/get_all_hotels",
            "/api/hotels/get_hotel_by_hotelId/**",
            "/api/hotels/get_hotels_by_location/**",
            "/eureka"
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));

    public static final List<String> superAdminEndpoints = List.of(
            "/api/users/add_new_admin"
    );

    public Predicate<ServerHttpRequest> isSuperAdminAccess =
            request -> superAdminEndpoints
                    .stream()
                    .anyMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));

    public static final List<String> adminEndpoints = List.of(
            "/api/hotels/add_new_hotel"
    );

    public Predicate<ServerHttpRequest> isAdminAccess =
            request -> adminEndpoints
                    .stream()
                    .anyMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));
}
