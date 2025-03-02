
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "configurations_systeme")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationSysteme {

    @Id
    private String cle;
    
    @Column(nullable = false, length = 1000)
    private String valeur;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime derniereModification;
    
    @Column(nullable = false)
    private String modifiePar;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeConfiguration type;
    
    public enum TypeConfiguration {
        SECURITE,
        PAIEMENT,
        NOTIFICATION,
        COMMISSION,
        PARRAINAGE,
        SYSTEME
    }
}
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "configurations_systeme")
public class ConfigurationSysteme {
    
    @Id
    @Column(name = "cle", nullable = false, unique = true)
    private String cle;
    
    @Column(name = "valeur", nullable = false, columnDefinition = "TEXT")
    private String valeur;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "type")
    private String type; // GENERAL, PAIEMENT, NOTIFICATION, COURSE, etc.
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
    
    @Column(name = "modifie_par")
    private String modifiePar;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}
