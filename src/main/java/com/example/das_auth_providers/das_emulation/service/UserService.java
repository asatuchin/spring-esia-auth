package com.example.das_auth_providers.das_emulation.service;

import com.example.das_auth_providers.das_emulation.entity.AuthProviders;
import com.example.das_auth_providers.das_emulation.entity.RegistrationParameters;
import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.repository.RoleRepository;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final URI registrationEndpointUri;

    public UserService(
            final UserRepository userRepository,
            final RoleRepository roleRepository,
            final BCryptPasswordEncoder bCryptPasswordEncoder,
            final @Value("${security.auth.registration-uri}") URI registrationEndpointUri
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.registrationEndpointUri = registrationEndpointUri;
    }

    public void save(final User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public URI getThirdPartyRegistrationLink(
            final AuthProviders provider,
            final long id,
            final Map<String, String> parameters
    ) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.setAll(parameters);
        return UriComponentsBuilder.fromUri(registrationEndpointUri)
                .queryParam(RegistrationParameters.THIRD_PARTY_PROVIDER, provider.getName())
                .queryParam(RegistrationParameters.THIRD_PARTY_ID, id)
                .queryParams(paramsMap)
                .build()
                .toUri();
    }
}
