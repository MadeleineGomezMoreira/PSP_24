package com.example.server.domain.usecases.security;

import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.data.repositories.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsUseCase implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DriverCredentialEntity credential = authRepository.findDriverCredentialEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                .username(credential.getUsername())
                .password(credential.getPassword())
                //is it okay if I am inputting the role and not the authorities? Do the authorities load on their own?
                .roles(credential.getRole().getRoleName())
                .build();
    }
}
