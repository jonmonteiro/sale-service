package sale_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String POST_PRODUCT = "/api/products";
    private static final String DELETE_PRODUCT = "/api/products/{id}";
    private static final String PUT_PRODUCT = "/api/products/{id}";
    private static final String GET_PRODUCTS = "/api/products";
    private static final String GET_PRODUCT = "/api/products/{id}";
    private static final String ROLE = "ADMIN";

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, POST_PRODUCT).hasRole(ROLE)
                        .requestMatchers(HttpMethod.DELETE, DELETE_PRODUCT).hasRole(ROLE)
                        .requestMatchers(HttpMethod.PUT, PUT_PRODUCT).hasRole(ROLE)
                        .requestMatchers(HttpMethod.GET, GET_PRODUCTS).hasRole(ROLE)
                        .requestMatchers(HttpMethod.GET, GET_PRODUCT).hasRole(ROLE)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
