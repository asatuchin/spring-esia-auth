package com.example.das_auth_providers.das_emulation.configuration;

import com.example.das_auth_providers.vk.security.VKAuthRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final VKAuthRequestFilter vkAuthRequestFilter;

    public SecurityConfiguration(
            final VKAuthRequestFilter vkAuthRequestFilter
    ) {
        this.vkAuthRequestFilter = vkAuthRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/**", "/error", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(vkAuthRequestFilter, BasicAuthenticationFilter.class)
                .httpBasic();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final List<AuthenticationProvider> authenticationProviders
    ) {
        return new ProviderManager(authenticationProviders);
    }
}