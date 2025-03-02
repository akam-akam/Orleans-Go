
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.VerificationDocumentsDTO;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.StatutVerification;
import com.orleansgo.administrateur.model.VerificationDocuments;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import com.orleansgo.administrateur.repository.VerificationDocumentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationDocumentsServiceTest {

    @Mock
    private VerificationDocumentsRepository verificationDocumentsRepository;

    @Mock
    private AdministrateurRepository administrateurRepository;

    @InjectMocks
    private VerificationDocumentsService verificationDocumentsService;

    private UUID chauffeurId;
    private UUID administrateurId;
    private UUID verificationId;
    private VerificationDocuments verification;
    private Administrateur administrateur;

    @BeforeEach
    void setUp() {
        chauffeurId = UUID.randomUUID();
        administrateurId = UUID.randomUUID();
        verificationId = UUID.randomUUID();

        verification = VerificationDocuments.builder()
                .id(verificationId)
                .chauffeurId(chauffeurId)
                .permisValide(false)
                .carteGriseValide(false)
                .assuranceValide(false)
                .statutVerification(StatutVerification.EN_ATTENTE)
                .dateCreation(LocalDateTime.now())
                .build();

        administrateur = new Administrateur();
        administrateur.setId(administrateurId);
        administrateur.setEmail("admin@orleansgo.com");
    }

    @Test
    void testGetAllVerifications() {
        // Given
        when(verificationDocumentsRepository.findAll()).thenReturn(Arrays.asList(verification));

        // When
        List<VerificationDocumentsDTO> result = verificationDocumentsService.getAllVerifications();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(verificationId, result.get(0).getId());
        assertEquals(chauffeurId, result.get(0).getChauffeurId());
        assertEquals(StatutVerification.EN_ATTENTE, result.get(0).getStatutVerification());
    }

    @Test
    void testGetVerificationById() {
        // Given
        when(verificationDocumentsRepository.findById(verificationId)).thenReturn(Optional.of(verification));

        // When
        VerificationDocumentsDTO result = verificationDocumentsService.getVerificationById(verificationId);

        // Then
        assertNotNull(result);
        assertEquals(verificationId, result.getId());
        assertEquals(chauffeurId, result.getChauffeurId());
    }

    @Test
    void testGetVerificationByIdNotFound() {
        // Given
        when(verificationDocumentsRepository.findById(verificationId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> verificationDocumentsService.getVerificationById(verificationId));
    }

    @Test
    void testCreateVerification() {
        // Given
        VerificationDocumentsDTO dto = new VerificationDocumentsDTO();
        dto.setChauffeurId(chauffeurId);

        when(verificationDocumentsRepository.save(any(VerificationDocuments.class))).thenReturn(verification);

        // When
        VerificationDocumentsDTO result = verificationDocumentsService.createVerification(dto);

        // Then
        assertNotNull(result);
        assertEquals(chauffeurId, result.getChauffeurId());
        assertEquals(StatutVerification.EN_ATTENTE, result.getStatutVerification());
        verify(verificationDocumentsRepository, times(1)).save(any(VerificationDocuments.class));
    }

    @Test
    void testUpdateVerification() {
        // Given
        VerificationDocumentsDTO dto = new VerificationDocumentsDTO();
        dto.setPermisValide(true);
        dto.setCarteGriseValide(true);
        dto.setAssuranceValide(true);
        dto.setCommentaires("Documents valides");

        when(administrateurRepository.findById(administrateurId)).thenReturn(Optional.of(administrateur));
        when(verificationDocumentsRepository.findById(verificationId)).thenReturn(Optional.of(verification));
        when(verificationDocumentsRepository.save(any(VerificationDocuments.class))).thenReturn(verification);

        // When
        VerificationDocumentsDTO result = verificationDocumentsService.updateVerification(verificationId, dto, administrateurId);

        // Then
        assertNotNull(result);
        assertEquals(verificationId, result.getId());
        assertTrue(result.isPermisValide());
        assertTrue(result.isCarteGriseValide());
        assertTrue(result.isAssuranceValide());
        assertEquals("Documents valides", result.getCommentaires());
        assertEquals(StatutVerification.VALIDE, result.getStatutVerification());
        verify(verificationDocumentsRepository, times(1)).save(any(VerificationDocuments.class));
    }
}
