
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
