package com.example.das_auth_providers.vk.service;

import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(
            final UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(""));
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return AuthorityUtils.createAuthorityList("ALL");
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public String getUsername() {
                    return user.getEmail();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("User " + email + " not found.", e);
        }
    }
}
