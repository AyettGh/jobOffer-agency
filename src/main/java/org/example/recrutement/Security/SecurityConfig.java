package org.example.recrutement.Security;

import org.example.recrutement.Entity.Enum.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                            .requestMatchers("/admin/**").hasAuthority(RoleEnum.ADMIN.name())
                            .requestMatchers("/offers/edit/**").hasAuthority(RoleEnum.ADMIN.name())
                            .requestMatchers("/agency/new").hasAuthority(RoleEnum.AGENCY.name())
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/login")
                            .successHandler(authenticationSuccessHandler())
                            .failureUrl("/login?error=true")
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/login")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .permitAll()
                    )
                    .exceptionHandling(ex -> ex
                            .accessDeniedPage("/access-denied")
                    );

            return http.build();
        }

        @Bean
        public AuthenticationSuccessHandler authenticationSuccessHandler() {
            return (request, response, authentication) -> {
                String targetUrl = "/redirect-by-role";
                response.sendRedirect(targetUrl);
            };
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        @Bean
        public AuthenticationManager authenticationManager(
                AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }


