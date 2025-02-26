package com.orleansgo.utilisateur.config;

import com.orleansgo.utilisateur.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAuthProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String email = auth.getName();
        String password = auth.getCredentials().toString();

        UserDetails user = userService.loadUserByUsername(email);
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
