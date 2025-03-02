
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, String> {
    
    @Query("SELECT c FROM Commission c WHERE c.dateEffective <= :date ORDER BY c.dateEffective DESC LIMIT 1")
    Optional<Commission> findActiveCommissionAtDate(LocalDateTime date);
}
