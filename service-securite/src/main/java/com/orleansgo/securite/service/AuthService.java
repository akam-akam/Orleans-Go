
package com.orleansgo.securite.service;

import com.orleansgo.securite.dto.AuthRequest;
import com.orleansgo.securite.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

        return new AuthResponse(accessToken, refreshToken, roles);
    }

    public AuthResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(token -> {
                    String username = token.getUsername();
                    refreshTokenService.verifyExpiration(token);
                    String newAccessToken = tokenProvider.generateTokenFromUsername(username);
                    
                    List<String> roles = tokenProvider.getRolesFromToken(newAccessToken);
                    
                    return new AuthResponse(newAccessToken, refreshToken, roles);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found in database"));
    }

    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
    }
}
