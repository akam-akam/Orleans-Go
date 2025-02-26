package com.orleansgo.utilisateur.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UtilisateurDTO {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Format de numéro invalide")
    private String numeroTelephone;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, message = "Le prénom doit contenir au moins 2 caractères")
    @Size(max = 50, message = "Le prénom doit contenir au plus 50 caractères")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, message = "Le nom doit contenir au moins 2 caractères")
    @Size(max = 50, message = "Le nom doit contenir au plus 50 caractères")
    private String nom;


    private boolean actif;
    private boolean emailVerifie;
    private boolean telephoneVerifie;
    private boolean support2FA;
    private String codeParrainage;
    private BigDecimal soldeBonus;
    private BigDecimal soldeRetirable;
    private LocalDateTime dateCreation;
}
