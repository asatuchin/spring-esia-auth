//package com.example.das_auth_providers.das_emulation.configuration;
//
//import com.example.das_auth_providers.common.security.DasAuthFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    private final List<DasAuthFilter> authFilters;
//
//    public SecurityConfiguration(
//            final List<DasAuthFilter> authFilters
//    ) {
//        this.authFilters = authFilters;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        HttpSecurity security =http.authorizeRequests()
//                .antMatchers("/user/**").permitAll()
//                .anyRequest().authenticated()
//                .and();
//        authFilters.forEach(security::addFilter);
//        security.httpBasic();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            final List<AuthenticationProvider> authenticationProviders
//    ) {
//        return new ProviderManager(authenticationProviders);
//    }
//}