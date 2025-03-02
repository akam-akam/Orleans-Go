
package com.orleansgo.commission.repository;

import com.orleansgo.commission.model.CommissionTrajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommissionTrajetRepository extends JpaRepository<CommissionTrajet, Long> {

    List<CommissionTrajet> findByChauffeurId(Long chauffeurId);

    List<CommissionTrajet> findByTrajetId(Long trajetId);

    List<CommissionTrajet> findByDateTrajetBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT SUM(ct.montantCommission) FROM CommissionTrajet ct WHERE ct.dateTrajet BETWEEN :debut AND :fin")
    BigDecimal calculateTotalCommissionForPeriod(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT SUM(ct.montantCommission) FROM CommissionTrajet ct WHERE ct.chauffeurId = :chauffeurId AND ct.dateTrajet BETWEEN :debut AND :fin")
    BigDecimal calculateTotalCommissionForChauffeur(Long chauffeurId, LocalDateTime debut, LocalDateTime fin);
}
