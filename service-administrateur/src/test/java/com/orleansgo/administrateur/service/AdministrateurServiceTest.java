
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.AdministrateurDTO;
import com.orleansgo.administrateur.exception.AdministrateurNotFoundException;
import com.orleansgo.administrateur.exception.EmailAlreadyExistsException;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.RoleAdministrateur;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdministrateurServiceTest {

    @Mock
    private AdministrateurRepository administrateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdministrateurService administrateurService;

    private Administrateur administrateur;
    private AdministrateurDTO administrateurDTO;

    @BeforeEach
    void setUp() {
        administrateur = new Administrateur();
        administrateur.setId("1");
        administrateur.setEmail("admin@orleansgo.com");
        administrateur.setMotDePasse("hashedPassword");
        administrateur.setNom("Admin");
        administrateur.setPrenom("Test");
        administrateur.setRole(RoleAdministrateur.ADMIN_STANDARD);
        administrateur.setActif(true);
        administrateur.setDateCreation(LocalDateTime.now());

        administrateurDTO = new AdministrateurDTO();
        administrateurDTO.setId("1");
        administrateurDTO.setEmail("admin@orleansgo.com");
        administrateurDTO.setNom("Admin");
        administrateurDTO.setPrenom("Test");
        administrateurDTO.setRole(RoleAdministrateur.ADMIN_STANDARD);
        administrateurDTO.setActif(true);
        administrateurDTO.setDateCreation(LocalDateTime.now());
    }

    @Test
    void getAllAdministrateurs_shouldReturnAllAdmins() {
        when(administrateurRepository.findAll()).thenReturn(Arrays.asList(administrateur));

        List<AdministrateurDTO> result = administrateurService.getAllAdministrateurs();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("admin@orleansgo.com");
    }

    @Test
    void getAdministrateur_whenExists_shouldReturnAdmin() {
        when(administrateurRepository.findById("1")).thenReturn(Optional.of(administrateur));

        AdministrateurDTO result = administrateurService.getAdministrateur("1");

        assertThat(result.getEmail()).isEqualTo("admin@orleansgo.com");
    }

    @Test
    void getAdministrateur_whenNotExists_shouldThrowException() {
        when(administrateurRepository.findById("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> administrateurService.getAdministrateur("1"))
                .isInstanceOf(AdministrateurNotFoundException.class);
    }

    @Test
    void createAdministrateur_whenEmailNotExists_shouldCreateAdmin() {
        when(administrateurRepository.existsByEmail("admin@orleansgo.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(administrateurRepository.save(any(Administrateur.class))).thenReturn(administrateur);

        AdministrateurDTO result = administrateurService.createAdministrateur(administrateurDTO, "password");

        assertThat(result.getEmail()).isEqualTo("admin@orleansgo.com");
        verify(administrateurRepository).save(any(Administrateur.class));
    }

    @Test
    void createAdministrateur_whenEmailExists_shouldThrowException() {
        when(administrateurRepository.existsByEmail("admin@orleansgo.com")).thenReturn(true);

        assertThatThrownBy(() -> administrateurService.createAdministrateur(administrateurDTO, "password"))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void updateAdministrateur_whenExists_shouldUpdateAdmin() {
        when(administrateurRepository.findById("1")).thenReturn(Optional.of(administrateur));
        when(administrateurRepository.save(any(Administrateur.class))).thenReturn(administrateur);

        AdministrateurDTO result = administrateurService.updateAdministrateur("1", administrateurDTO);

        assertThat(result.getEmail()).isEqualTo("admin@orleansgo.com");
        verify(administrateurRepository).save(any(Administrateur.class));
    }

    @Test
    void deleteAdministrateur_whenExists_shouldDeleteAdmin() {
        when(administrateurRepository.existsById("1")).thenReturn(true);

        administrateurService.deleteAdministrateur("1");

        verify(administrateurRepository).deleteById("1");
    }

    @Test
    void changeAdministrateurRole_whenExists_shouldChangeRole() {
        when(administrateurRepository.findById("1")).thenReturn(Optional.of(administrateur));
        when(administrateurRepository.save(any(Administrateur.class))).thenReturn(administrateur);

        AdministrateurDTO result = administrateurService.changeAdministrateurRole("1", RoleAdministrateur.SUPER_ADMIN);

        assertThat(result.getEmail()).isEqualTo("admin@orleansgo.com");
        verify(administrateurRepository).save(any(Administrateur.class));
    }
}
