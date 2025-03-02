
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
package com.orleansgo.administrateur.service;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministrateurServiceTest {

    @Mock
    private AdministrateurRepository administrateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdministrateurService administrateurService;

    private Administrateur administrateur;

    @BeforeEach
    void setUp() {
        administrateur = new Administrateur();
        administrateur.setId(1L);
        administrateur.setNom("Dupont");
        administrateur.setPrenom("Jean");
        administrateur.setEmail("jean.dupont@example.com");
        administrateur.setMotDePasse("password");
        administrateur.setActif(true);
        administrateur.setRole(RoleAdministrateur.ADMIN_OPERATIONS);
    }

    @Test
    void findAllAdministrateurs_shouldReturnAllAdministrateurs() {
        // Given
        List<Administrateur> expectedAdmins = Arrays.asList(administrateur);
        when(administrateurRepository.findAll()).thenReturn(expectedAdmins);

        // When
        List<Administrateur> actualAdmins = administrateurService.findAllAdministrateurs();

        // Then
        assertEquals(expectedAdmins, actualAdmins);
        verify(administrateurRepository).findAll();
    }

    @Test
    void findAdministrateurById_whenAdminExists_shouldReturnAdmin() {
        // Given
        when(administrateurRepository.findById(1L)).thenReturn(Optional.of(administrateur));

        // When
        Optional<Administrateur> result = administrateurService.findAdministrateurById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(administrateur, result.get());
        verify(administrateurRepository).findById(1L);
    }

    @Test
    void findAdministrateurById_whenAdminDoesNotExist_shouldReturnEmpty() {
        // Given
        when(administrateurRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Administrateur> result = administrateurService.findAdministrateurById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(administrateurRepository).findById(1L);
    }

    @Test
    void createAdministrateur_whenEmailExists_shouldThrowRuntimeException() {
        // Given
        when(administrateurRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> administrateurService.createAdministrateur(administrateur));
        verify(administrateurRepository).existsByEmail(administrateur.getEmail());
        verify(administrateurRepository, never()).save(any(Administrateur.class));
    }

    @Test
    void createAdministrateur_whenEmailDoesNotExist_shouldCreateAdmin() {
        // Given
        when(administrateurRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(administrateurRepository.save(any(Administrateur.class))).thenReturn(administrateur);

        // When
        Administrateur result = administrateurService.createAdministrateur(administrateur);

        // Then
        assertEquals(administrateur, result);
        verify(administrateurRepository).existsByEmail(administrateur.getEmail());
        verify(passwordEncoder).encode(administrateur.getMotDePasse());
        verify(administrateurRepository).save(administrateur);
    }

    @Test
    void updateAdministrateur_whenAdminExists_shouldUpdateAdminDetails() {
        // Given
        Administrateur updatedAdmin = new Administrateur();
        updatedAdmin.setNom("Smith");
        updatedAdmin.setPrenom("John");
        updatedAdmin.setRole(RoleAdministrateur.ADMIN_SUPPORT);
        updatedAdmin.setActif(false);

        when(administrateurRepository.findById(1L)).thenReturn(Optional.of(administrateur));
        when(administrateurRepository.save(any(Administrateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Administrateur> result = administrateurService.updateAdministrateur(1L, updatedAdmin);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Smith", result.get().getNom());
        assertEquals("John", result.get().getPrenom());
        assertEquals(RoleAdministrateur.ADMIN_SUPPORT, result.get().getRole());
        assertFalse(result.get().isActif());
        verify(administrateurRepository).findById(1L);
        verify(administrateurRepository).save(administrateur);
    }

    @Test
    void updateAdministrateur_whenAdminDoesNotExist_shouldReturnEmpty() {
        // Given
        Administrateur updatedAdmin = new Administrateur();
        when(administrateurRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Administrateur> result = administrateurService.updateAdministrateur(1L, updatedAdmin);

        // Then
        assertFalse(result.isPresent());
        verify(administrateurRepository).findById(1L);
        verify(administrateurRepository, never()).save(any(Administrateur.class));
    }
    
    @Test
    void deleteAdministrateur_shouldCallDeleteById() {
        // When
        administrateurService.deleteAdministrateur(1L);

        // Then
        verify(administrateurRepository).deleteById(1L);
    }
}
