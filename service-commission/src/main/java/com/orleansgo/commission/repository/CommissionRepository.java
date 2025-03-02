
package com.orleansgo.commission.repository;

import com.orleansgo.commission.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {

    List<Commission> findByActive(boolean active);

    @Query("SELECT c FROM Commission c WHERE c.active = true AND " +
           "(c.dateDebut <= :now AND (c.dateFin IS NULL OR c.dateFin >= :now))")
    List<Commission> findActiveCommissions(LocalDateTime now);

    Optional<Commission> findByNomAndActive(String nom, boolean active);
}
