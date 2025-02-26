package com.orleansgo.utilisateur.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Parrainage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "parrain_id", nullable = false)
    private Utilisateur parrain;

    @ManyToOne
    @JoinColumn(name = "filleul_id", nullable = false)
    private Utilisateur filleul;

    private BigDecimal bonusParrain;
    private BigDecimal bonusFilleul;
    private boolean valide;
    private LocalDateTime dateCreation = LocalDateTime.now();

    public void validerParrainage() {
        this.valide = true;
    }
}
