package com.orleansgo.utilisateur.dto;


import java.util.Set;

public record EnableMfaResponse(
        String qrCodeUrl,
        String secret,
        Set<String> backupCodes
) {}
