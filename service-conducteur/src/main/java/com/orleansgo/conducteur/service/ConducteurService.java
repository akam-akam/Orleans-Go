package com.orleansgo.conducteur.service;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.exception.ResourceNotFoundException;
import com.orleansgo.conducteur.mapper.ConducteurMapper;
import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.repository.ConducteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConducteurService {

    private final ConducteurRepository conducteurRepository;
    private final ConducteurMapper conducteurMapper;

    public ConducteurDTO creerConducteur(ConducteurDTO conducteurDTO) {
        Conducteur conducteur = conducteurMapper.toEntity(conducteurDTO);
        return conducteurMapper.toDTO(conducteurRepository.save(conducteur));
    }

    public List<ConducteurDTO> listerTousLesConducteurs() {
        return conducteurRepository.findAll().stream()
                .map(conducteurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ConducteurDTO trouverParId(UUID id) {
        return conducteurRepository.findById(id)
                .map(conducteurMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé"));
    }

    public void supprimerConducteur(UUID id) {
        conducteurRepository.deleteById(id);
    }
}
package com.orleansgo.conducteur.service;

import com.orleansgo.conducteur.dto.ConducteurDTO;
import com.orleansgo.conducteur.exception.ResourceNotFoundException;
import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.repository.ConducteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConducteurService {

    private final ConducteurRepository conducteurRepository;

    @Transactional(readOnly = true)
    public List<ConducteurDTO> getAllConducteurs() {
        return conducteurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConducteurDTO getConducteurById(UUID id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        return convertToDTO(conducteur);
    }

    @Transactional(readOnly = true)
    public ConducteurDTO getConducteurByUtilisateurId(UUID utilisateurId) {
        Conducteur conducteur = conducteurRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé pour l'utilisateur ID: " + utilisateurId));
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO createConducteur(UUID utilisateurId) {
        if (conducteurRepository.findByUtilisateurId(utilisateurId).isPresent()) {
            throw new IllegalStateException("Un conducteur existe déjà pour cet utilisateur");
        }
        
        Conducteur conducteur = new Conducteur();
        conducteur.setUtilisateurId(utilisateurId);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO updateDisponibilite(UUID id, boolean disponible) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        
        conducteur.setDisponible(disponible);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO ajouterGains(UUID id, BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        
        conducteur.ajouterGains(montant);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    @Transactional
    public ConducteurDTO updateDocumentsValides(UUID id, boolean documentsValides) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur non trouvé avec l'ID: " + id));
        
        conducteur.setDocumentsValides(documentsValides);
        conducteur = conducteurRepository.save(conducteur);
        return convertToDTO(conducteur);
    }

    private ConducteurDTO convertToDTO(Conducteur conducteur) {
        return new ConducteurDTO(
                conducteur.getId(),
                conducteur.getUtilisateurId(),
                conducteur.isDisponible(),
                conducteur.getNote(),
                conducteur.getTotalCourses(),
                conducteur.getGains(),
                conducteur.isDocumentsValides()
        );
    }
}
