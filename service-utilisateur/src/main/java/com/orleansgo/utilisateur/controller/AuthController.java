package com.orleansgo.utilisateur.controller;

import com.orleansgo.utilisateur.dto.*;
import com.orleansgo.utilisateur.security.jwt.JwtUtils;
import com.orleansgo.utilisateur.service.PasswordResetService;
import com.orleansgo.utilisateur.service.TotpService;
import com.orleansgo.utilisateur.service.UserDetailsServiceImpl;
import com.orleansgo.utilisateur.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthStrategyFactory authFactory;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthStrategy strategy = authFactory.getStrategy(request.getType());
        Authentication auth = strategy.authenticate(request);

        String token = jwtUtils.generateToken(auth);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<AuthResponse> oauthCallback(
            @RequestParam String code,
            @RequestParam String provider
    ) {
        OAuth2AuthRequest request = new OAuth2AuthRequest(provider, code);
        return login(request);
    }
}
