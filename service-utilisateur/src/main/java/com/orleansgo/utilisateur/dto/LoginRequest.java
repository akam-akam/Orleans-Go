package com.orleansgo.utilisateur.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requête d'authentification")
public record LoginRequest(
        @Schema(description = "Email de l'utilisateur", example = "user@example.com", required = true)
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @Schema(description = "Mot de passe", example = "MonSuperPassword123!", required = true)
        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {}
