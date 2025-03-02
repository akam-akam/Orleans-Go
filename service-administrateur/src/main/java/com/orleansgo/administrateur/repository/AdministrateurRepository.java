
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, String> {
    Optional<Administrateur> findByEmail(String email);
    boolean existsByEmail(String email);
}
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByEmail(String email);
    boolean existsByEmail(String email);
}
