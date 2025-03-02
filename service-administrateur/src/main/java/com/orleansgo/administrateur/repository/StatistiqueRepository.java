
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.Statistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatistiqueRepository extends JpaRepository<Statistique, Long> {
    
    List<Statistique> findByType(String type);
    
    List<Statistique> findByPeriodeBetween(LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT s FROM Statistique s WHERE s.type = ?1 AND s.periode BETWEEN ?2 AND ?3")
    List<Statistique> findByTypeAndPeriode(String type, LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT SUM(s.valeur) FROM Statistique s WHERE s.type = ?1 AND s.periode BETWEEN ?2 AND ?3")
    Double calculateSumByTypeAndPeriode(String type, LocalDateTime debut, LocalDateTime fin);
}
