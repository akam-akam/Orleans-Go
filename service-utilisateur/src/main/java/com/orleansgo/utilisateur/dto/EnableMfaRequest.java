package com.orleansgo.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnableMfaRequest(
        @NotBlank @Size(min = 6, max = 6) String code
) {}
