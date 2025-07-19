package com.sample.graphql.configuration;

import com.sample.graphql.filter.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain springWebFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/graphiql/**").permitAll()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling(Customizer.withDefaults());
        return http.build();

    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

}
