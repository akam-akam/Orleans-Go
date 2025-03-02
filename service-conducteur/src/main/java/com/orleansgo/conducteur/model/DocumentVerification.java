
package com.orleansgo.conducteur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;

    private boolean permisValide = false;

    private boolean carteGriseValide = false;

    private boolean assuranceValide = false;

    private boolean valideParAdmin = false;

    private LocalDateTime dateVerification;

    @Version
    private Long version;
    
    public boolean estComplet() {
        return permisValide && carteGriseValide && assuranceValide && valideParAdmin;
    }
    
    public void validerDocument(String typeDocument) {
        switch (typeDocument.toLowerCase()) {
            case "permis":
                this.permisValide = true;
                break;
            case "carte_grise":
                this.carteGriseValide = true;
                break;
            case "assurance":
                this.assuranceValide = true;
                break;
            default:
                throw new IllegalArgumentException("Type de document non reconnu: " + typeDocument);
        }
        this.dateVerification = LocalDateTime.now();
    }
}
