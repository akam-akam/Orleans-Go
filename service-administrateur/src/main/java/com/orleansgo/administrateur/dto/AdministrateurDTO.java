
package com.orleansgo.administrateur.dto;

import com.orleansgo.administrateur.model.RoleAdministrateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrateurDTO {
    private String id;
    private String email;
    private String nom;
    private String prenom;
    private RoleAdministrateur role;
    private boolean actif;
    private LocalDateTime derniereConnexion;
    private LocalDateTime dateCreation;
}
