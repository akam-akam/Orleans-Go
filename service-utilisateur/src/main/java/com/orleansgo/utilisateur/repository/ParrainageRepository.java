package com.orleansgo.utilisateur.repository;


import com.orleansgo.utilisateur.model.Parrainage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ParrainageRepository extends JpaRepository<Parrainage, UUID> {
    List<Parrainage> findByParrainId(UUID parrainId);
    List<Parrainage> findByFilleulId(UUID filleulId);
}
