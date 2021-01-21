package com.example.das_auth_providers.das_emulation.configuration;

import com.example.das_auth_providers.das_emulation.security.DasAuthFailureHandler;
import com.example.das_auth_providers.das_emulation.security.DasAuthFilter;
import com.example.das_auth_providers.das_emulation.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final List<DasAuthFilter> authFilters;
    private final List<AuthenticationProvider> authProviders;

    WebSecurityConfig(
            final UserDetailsServiceImpl userDetailsService,
            final List<DasAuthFilter> authFilters,
            final List<AuthenticationProvider> authProviders
    ) {
        this.userDetailsService = userDetailsService;
        this.authFilters = authFilters;
        this.authProviders = authProviders;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration", "/vk/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .failureHandler(authenticationFailureHandler())
                .loginPage("/login")
                .defaultSuccessUrl("/welcome")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        authFilters.forEach(f -> http.addFilterAfter(f, BasicAuthenticationFilter.class));
        authProviders.forEach(http::authenticationProvider);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new DasAuthFailureHandler();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(
            final List<AuthenticationProvider> authProviders
    ) {
        return new ProviderManager(authProviders);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}