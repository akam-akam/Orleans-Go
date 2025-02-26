package com.orleansgo.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmResetRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 8) String newPassword
) {}
