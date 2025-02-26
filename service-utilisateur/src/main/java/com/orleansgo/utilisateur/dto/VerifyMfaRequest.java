package com.orleansgo.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyMfaRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 6, max = 6) String code
) {}
