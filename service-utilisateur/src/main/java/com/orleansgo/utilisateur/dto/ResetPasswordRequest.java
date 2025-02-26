package com.orleansgo.utilisateur.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record ResetPasswordRequest(
        @NotBlank @Email String email
) {}
