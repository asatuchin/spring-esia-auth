package com.example.das_auth_providers.das_emulation.configuration;

import com.example.das_auth_providers.das_emulation.security.DasAuthFailureEntryPoint;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final List<DasAuthFilter> authFilters;
    private final List<AuthenticationProvider> authProviders;

    private final DasAuthFailureEntryPoint accessDeniedHandler;

    private final PasswordEncoder bCryptPasswordEncoder;

    WebSecurityConfig(
            final UserDetailsServiceImpl userDetailsService,
            final List<DasAuthFilter> authFilters,
            final List<AuthenticationProvider> authProviders,
            final DasAuthFailureEntryPoint accessDeniedHandler,
            final PasswordEncoder bCryptPasswordEncoder
    ) {
        this.userDetailsService = userDetailsService;
        this.authFilters = authFilters;
        this.authProviders = authProviders;
        this.accessDeniedHandler = accessDeniedHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration", "/esia/**", "/vk/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
        authFilters.forEach(f -> http.addFilterAfter(f, BasicAuthenticationFilter.class));
        authProviders.forEach(http::authenticationProvider);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(
            final List<AuthenticationProvider> authProviders
    ) {
        return new ProviderManager(authProviders);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}