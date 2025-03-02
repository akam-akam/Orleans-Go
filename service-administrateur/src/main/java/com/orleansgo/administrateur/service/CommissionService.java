
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.CommissionDTO;
import com.orleansgo.administrateur.exception.AdministrateurNotFoundException;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Commission;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import com.orleansgo.administrateur.repository.CommissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommissionService {
    
    private final CommissionRepository commissionRepository;
    private final AdministrateurRepository administrateurRepository;
    
    public List<CommissionDTO> getAllCommissions() {
        return commissionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CommissionDTO getCommission(String id) {
        return commissionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Commission non trouvée avec l'ID: " + id));
    }
    
    public CommissionDTO getActiveCommission() {
        return commissionRepository.findActiveCommissionAtDate(LocalDateTime.now())
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Aucune commission active trouvée"));
    }
    
    @Transactional
    public CommissionDTO createCommission(CommissionDTO commissionDTO) {
        Administrateur administrateur = administrateurRepository.findById(commissionDTO.getAdministrateurId())
                .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + commissionDTO.getAdministrateurId()));
        
        Commission commission = new Commission();
        commission.setTauxCommission(commissionDTO.getTauxCommission());
        commission.setBonusParrain(commissionDTO.getBonusParrain());
        commission.setBonusFilleul(commissionDTO.getBonusFilleul());
        commission.setDateEffective(commissionDTO.getDateEffective());
        commission.setDescription(commissionDTO.getDescription());
        commission.setAdministrateur(administrateur);
        commission.setDateCreation(LocalDateTime.now());
        
        return convertToDTO(commissionRepository.save(commission));
    }
    
    @Transactional
    public CommissionDTO updateCommission(String id, CommissionDTO commissionDTO) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission non trouvée avec l'ID: " + id));
        
        Administrateur administrateur = null;
        if (commissionDTO.getAdministrateurId() != null) {
            administrateur = administrateurRepository.findById(commissionDTO.getAdministrateurId())
                    .orElseThrow(() -> new AdministrateurNotFoundException("Administrateur non trouvé avec l'ID: " + commissionDTO.getAdministrateurId()));
        }
        
        commission.setTauxCommission(commissionDTO.getTauxCommission());
        commission.setBonusParrain(commissionDTO.getBonusParrain());
        commission.setBonusFilleul(commissionDTO.getBonusFilleul());
        commission.setDateEffective(commissionDTO.getDateEffective());
        commission.setDescription(commissionDTO.getDescription());
        
        if (administrateur != null) {
            commission.setAdministrateur(administrateur);
        }
        
        return convertToDTO(commissionRepository.save(commission));
    }
    
    @Transactional
    public void deleteCommission(String id) {
        if (!commissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commission non trouvée avec l'ID: " + id);
        }
        commissionRepository.deleteById(id);
    }
    
    private CommissionDTO convertToDTO(Commission commission) {
        CommissionDTO dto = new CommissionDTO();
        dto.setId(commission.getId());
        dto.setTauxCommission(commission.getTauxCommission());
        dto.setBonusParrain(commission.getBonusParrain());
        dto.setBonusFilleul(commission.getBonusFilleul());
        dto.setDateEffective(commission.getDateEffective());
        dto.setDateCreation(commission.getDateCreation());
        dto.setAdministrateurId(commission.getAdministrateur() != null ? commission.getAdministrateur().getId() : null);
        dto.setDescription(commission.getDescription());
        return dto;
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.Commission;
import com.orleansgo.administrateur.repository.CommissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommissionService {
    private final CommissionRepository commissionRepository;
    
    public List<Commission> findAllCommissions() {
        return commissionRepository.findAll();
    }
    
    public Optional<Commission> findCommissionById(Long id) {
        return commissionRepository.findById(id);
    }
    
    public Optional<Commission> findCurrentCommission() {
        return commissionRepository.findCurrentCommission();
    }
    
    @Transactional
    public Commission createCommission(Commission commission, Administrateur administrateur) {
        commission.setModifiePar(administrateur);
        return commissionRepository.save(commission);
    }
    
    @Transactional
    public Optional<Commission> updateCommission(Long id, Commission commissionDetails, Administrateur administrateur) {
        return commissionRepository.findById(id)
                .map(commission -> {
                    if (commission.getDateFin() != null) {
                        throw new RuntimeException("Impossible de modifier une commission qui a déjà été clôturée");
                    }
                    
                    commission.setTauxCommission(commissionDetails.getTauxCommission());
                    commission.setDescription(commissionDetails.getDescription());
                    commission.setModifiePar(administrateur);
                    commission.setDateModification(LocalDateTime.now());
                    
                    return commissionRepository.save(commission);
                });
    }
    
    @Transactional
    public Optional<Commission> clotureCommission(Long id, Administrateur administrateur) {
        return commissionRepository.findById(id)
                .map(commission -> {
                    if (commission.getDateFin() != null) {
                        throw new RuntimeException("Cette commission a déjà été clôturée");
                    }
                    
                    commission.setDateFin(LocalDateTime.now());
                    commission.setModifiePar(administrateur);
                    commission.setDateModification(LocalDateTime.now());
                    
                    return commissionRepository.save(commission);
                });
    }
    
    @Transactional
    public Commission ajusterCommission(BigDecimal nouveauTaux, Administrateur administrateur) {
        // On clôture la commission actuelle si elle existe
        Optional<Commission> commissionActuelle = findCurrentCommission();
        if (commissionActuelle.isPresent()) {
            clotureCommission(commissionActuelle.get().getId(), administrateur);
        }
        
        // On crée une nouvelle commission
        Commission nouvelleCommission = new Commission();
        nouvelleCommission.setTauxCommission(nouveauTaux);
        nouvelleCommission.setDateDebut(LocalDateTime.now());
        nouvelleCommission.setDescription("Ajustement du taux de commission");
        nouvelleCommission.setModifiePar(administrateur);
        
        return commissionRepository.save(nouvelleCommission);
    }
}
