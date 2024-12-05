package com.example.library2.configuration;

import com.example.library2.jwt.JWTTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTTokenFilter jwtTokenFilter;

    private static final String ADMIN_ENDPOINT_BOOK = "/book/admin/**";
    private static final String ADMIN_ENDPOINT_LIBRARY = "/library/admin/**";
    private static final String LOGIN_ENDPOINT = "/user/login";
    private static final String REG_ENDPOINT_USER = "/user/reg";
    private static final String REG_ENDPOINT_ADMIN = "/user/reg/admin";
    private static final String ADMIN = "ADMIN";
    private static final String DB = "/db/**";
  /*private static final String DELETE_USER = "/user/current";
    private static final String UPDATE_USER = "/user/updateUser"; */

    private static final String[] PUBLIC_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "configuration/**",
            "webjars/**",
            "/*.html",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(REG_ENDPOINT_USER).permitAll()
                .antMatchers(REG_ENDPOINT_ADMIN).permitAll()
               /* .antMatchers(DELETE_USER).authenticated()
                .antMatchers(UPDATE_USER).authenticated()*/
                .antMatchers(ADMIN_ENDPOINT_BOOK).hasAuthority(ADMIN)
                .antMatchers(ADMIN_ENDPOINT_LIBRARY).hasAuthority(ADMIN)
                .antMatchers(HttpMethod.GET, PUBLIC_URLS).permitAll()
                .antMatchers(DB).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
