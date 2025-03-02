
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "administrateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    private String motDePasse;
    
    private boolean actif = true;
    
    private String nom;
    
    private String prenom;
    
    @Enumerated(EnumType.STRING)
    private RoleAdministrateur role = RoleAdministrateur.ADMIN_STANDARD;
    
    private LocalDateTime derniereConnexion;
    
    private LocalDateTime dateCreation = LocalDateTime.now();
}
