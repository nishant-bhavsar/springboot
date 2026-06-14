package com.demo.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Wires up Spring Security for this demo app.
 *
 * In a real service you would load users from a database,
 * but for a demo, an in-memory store is perfectly fine.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines two hardcoded demo users:
     *   - alice / password123  (role: USER)
     *   - admin / admin123     (role: ADMIN)
     *
     * Passwords are stored as BCrypt hashes, never plain text.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails alice = User.builder()
                .username("alice")
                .password(encoder.encode("password123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(alice, admin);
    }

    /**
     * BCrypt is the recommended password hashing algorithm.
     * Spring Security uses this bean automatically when checking credentials.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines which URLs are public, which require login, and configures
     * the login/logout behaviour.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                // CSS, health check, and the login page itself are always public
                .requestMatchers("/css/**", "/actuator/health", "/login").permitAll()
                // Every other page requires the user to be logged in
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")          // our custom login page
                .defaultSuccessUrl("/dashboard", true) // where to go after login
                .failureUrl("/login?error")   // where to go on bad credentials
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // where to go after logout
                .permitAll()
            );

        return http.build();
    }

}
