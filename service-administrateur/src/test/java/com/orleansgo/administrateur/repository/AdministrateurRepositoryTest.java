
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.RoleAdministrateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AdministrateurRepositoryTest {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Test
    public void findByEmail_shouldReturnAdmin_whenEmailExists() {
        // Arrange
        Administrateur administrateur = new Administrateur();
        administrateur.setEmail("test@orleansgo.com");
        administrateur.setMotDePasse("password");
        administrateur.setNom("Test");
        administrateur.setPrenom("Admin");
        administrateur.setRole(RoleAdministrateur.ADMIN_STANDARD);
        administrateur.setActif(true);
        administrateur.setDateCreation(LocalDateTime.now());
        
        administrateurRepository.save(administrateur);

        // Act
        Optional<Administrateur> found = administrateurRepository.findByEmail("test@orleansgo.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@orleansgo.com");
    }

    @Test
    public void existsByEmail_shouldReturnTrue_whenEmailExists() {
        // Arrange
        Administrateur administrateur = new Administrateur();
        administrateur.setEmail("test@orleansgo.com");
        administrateur.setMotDePasse("password");
        administrateur.setNom("Test");
        administrateur.setPrenom("Admin");
        administrateur.setRole(RoleAdministrateur.ADMIN_STANDARD);
        administrateur.setActif(true);
        administrateur.setDateCreation(LocalDateTime.now());
        
        administrateurRepository.save(administrateur);

        // Act
        boolean exists = administrateurRepository.existsByEmail("test@orleansgo.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        // Act
        boolean exists = administrateurRepository.existsByEmail("nonexistent@orleansgo.com");

        // Assert
        assertThat(exists).isFalse();
    }
}
