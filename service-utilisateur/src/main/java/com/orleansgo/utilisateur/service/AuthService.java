
package com.orleansgo.utilisateur.service;

import com.orleansgo.utilisateur.dto.AuthRequest;
import com.orleansgo.utilisateur.dto.AuthResponse;
import com.orleansgo.utilisateur.exception.AuthenticationFailedException;
import com.orleansgo.utilisateur.exception.ResourceNotFoundException;
import com.orleansgo.utilisateur.exception.TokenExpiredException;
import com.orleansgo.utilisateur.model.User;
import com.orleansgo.utilisateur.repository.UserRepository;
import com.orleansgo.utilisateur.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TwoFactorService twoFactorService;

    @Transactional
    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            // Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

            // Vérifier si l'authentification à deux facteurs est activée
            if (user.isTwoFactorEnabled()) {
                // Si le code 2FA n'est pas fourni, demander à l'utilisateur de le fournir
                if (authRequest.getTwoFactorCode() == null || authRequest.getTwoFactorCode().isEmpty()) {
                    return AuthResponse.builder()
                            .twoFactorRequired(true)
                            .email(user.getEmail())
                            .build();
                }

                // Vérifier le code 2FA
                if (!twoFactorService.validateCode(user.getTwoFactorSecret(), authRequest.getTwoFactorCode())) {
                    throw new AuthenticationFailedException("Code d'authentification à deux facteurs invalide");
                }
            }

            // Mettre à jour la dernière connexion
            userService.updateLastLogin(user.getEmail());

            // Générer les tokens
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            // Construire la réponse
            return AuthResponse.builder()
                    .token(jwt)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .roles(user.getRoles())
                    .twoFactorRequired(false)
                    .build();

        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Email ou mot de passe incorrect");
        }
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) throws TokenExpiredException {
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new TokenExpiredException("Token expiré");
        }

        String email = jwtUtils.getUserNameFromJwtToken(refreshToken);
        UserDetails userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String newAccessToken = jwtUtils.generateJwtToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

        User user = (User) userDetails;
        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }
}
