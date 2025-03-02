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
package com.orleansgo.utilisateur.controller;

import com.orleansgo.utilisateur.dto.AuthRequest;
import com.orleansgo.utilisateur.dto.AuthResponse;
import com.orleansgo.utilisateur.dto.ErrorResponse;
import com.orleansgo.utilisateur.dto.UserRegistrationRequest;
import com.orleansgo.utilisateur.exception.AuthenticationFailedException;
import com.orleansgo.utilisateur.exception.EmailAlreadyExistsException;
import com.orleansgo.utilisateur.exception.PhoneNumberAlreadyExistsException;
import com.orleansgo.utilisateur.model.User;
import com.orleansgo.utilisateur.service.AuthService;
import com.orleansgo.utilisateur.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            User user = userService.registerUser(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT));
        } catch (PhoneNumberAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de l'enregistrement: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = authService.authenticate(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur d'authentification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        try {
            AuthResponse authResponse = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Erreur lors du rafraîchissement du token: " + e.getMessage(), HttpStatus.UNAUTHORIZED));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            // Dans une implémentation réelle, le token serait vérifié ici
            // Pour cette démo, on extrait juste l'email du token (ce qui n'est pas sécurisé)
            String email = token; // Dans la réalité, on décoderait le token
            User user = userService.verifyEmail(email);
            return ResponseEntity.ok("Email vérifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Erreur de vérification d'email: " + e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @PostMapping("/verify-phone")
    public ResponseEntity<?> verifyPhone(@RequestParam String phoneNumber, @RequestParam String code) {
        try {
            // Vérifier le code dans une implémentation réelle
            User user = userService.verifyPhone(phoneNumber);
            return ResponseEntity.ok("Numéro de téléphone vérifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Erreur de vérification de téléphone: " + e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }
}
