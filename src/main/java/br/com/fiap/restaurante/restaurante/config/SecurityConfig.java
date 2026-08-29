package br.com.fiap.restaurante.restaurante.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Temporary solution to allow all requests to be without authorization
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Allow all incoming requests
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 2. Disable CSRF (Highly recommended if building stateless REST APIs)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
