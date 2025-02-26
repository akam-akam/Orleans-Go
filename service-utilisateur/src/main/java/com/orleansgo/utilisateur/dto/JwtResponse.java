package com.orleansgo.utilisateur.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Schema(description = "Réponse JWT")
public record JwtResponse(
        @Schema(description = "Token JWT", example = "xxxxx.yyyyy.zzzzz")
        String token,

        @Schema(description = "ID utilisateur", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,

        @Schema(description = "Email utilisateur", example = "user@example.com")
        String email,

        @Schema(description = "Rôles de l'utilisateur")
        Collection<? extends GrantedAuthority> roles
) {}
