
package com.orleansgo.commission.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commissions_trajets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trajet_id", nullable = false)
    private Long trajetId;

    @Column(name = "montant_total_trajet", nullable = false)
    private BigDecimal montantTotalTrajet;

    @Column(name = "montant_commission", nullable = false)
    private BigDecimal montantCommission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

    @Column(name = "date_trajet", nullable = false)
    private LocalDateTime dateTrajet;

    @Column(name = "chauffeur_id", nullable = false)
    private Long chauffeurId;

    @Column(name = "passager_id", nullable = false)
    private Long passagerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
