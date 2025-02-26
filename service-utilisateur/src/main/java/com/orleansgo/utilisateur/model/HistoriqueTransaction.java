package com.orleansgo.utilisateur.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class HistoriqueTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal montant;
    private LocalDateTime dateTransaction = LocalDateTime.now();

    public void enregistrerTransaction() {
        // Logique pour enregistrer la transaction
    }
}
