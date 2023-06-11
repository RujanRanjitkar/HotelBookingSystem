package com.example.apigateway.filter;

import com.example.apigateway.customException.MustLoginException;
import com.example.apigateway.customException.RoleNotMatchException;
import com.example.apigateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    private Logger logger= LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new MustLoginException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                jwtUtil.validateToken(authHeader);

                String role = jwtUtil.extractUserRole(authHeader);

                if (validator.isSuperAdminAccess.test(exchange.getRequest())) {
                    if (!role.contains("ROLE_SUPER_ADMIN")) {
                        logger.error("User with role "+role+"  dont have access to this api");
                        throw new RoleNotMatchException("Only Super Admin Has Access");
                    }
                    logger.info("User With Role " + role + " Is Accessing The Api " + exchange.getRequest().getURI());
                }
                if(validator.isAdminAccess.test(exchange.getRequest())){
                    if(!role.contains("ROLE_ADMIN")){
                        logger.error("User with role "+role+"  dont have access to this api");
                        throw new RoleNotMatchException("Only Admin can add new hotel");
                    }
                    logger.info("User With Role " + role + " Is Accessing The Api " + exchange.getRequest().getURI());
                }
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
