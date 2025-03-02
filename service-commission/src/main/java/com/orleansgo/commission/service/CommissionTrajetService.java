
package com.orleansgo.commission.service;

import com.orleansgo.commission.dto.CommissionTrajetDTO;
import com.orleansgo.commission.exception.CommissionNotFoundException;
import com.orleansgo.commission.exception.CommissionTrajetNotFoundException;
import com.orleansgo.commission.model.Commission;
import com.orleansgo.commission.model.CommissionTrajet;
import com.orleansgo.commission.model.TypeCommission;
import com.orleansgo.commission.repository.CommissionRepository;
import com.orleansgo.commission.repository.CommissionTrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommissionTrajetService {

    private final CommissionTrajetRepository commissionTrajetRepository;
    private final CommissionRepository commissionRepository;

    public List<CommissionTrajetDTO> getAllCommissionsTrajet() {
        return commissionTrajetRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommissionTrajetDTO getCommissionTrajetById(Long id) {
        CommissionTrajet commissionTrajet = commissionTrajetRepository.findById(id)
                .orElseThrow(() -> new CommissionTrajetNotFoundException("Commission de trajet non trouvée avec l'ID: " + id));
        return convertToDTO(commissionTrajet);
    }

    public List<CommissionTrajetDTO> getCommissionsTrajetByChauffeur(Long chauffeurId) {
        return commissionTrajetRepository.findByChauffeurId(chauffeurId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CommissionTrajetDTO> getCommissionsTrajetByTrajet(Long trajetId) {
        return commissionTrajetRepository.findByTrajetId(trajetId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CommissionTrajetDTO> getCommissionsTrajetByPeriode(LocalDateTime debut, LocalDateTime fin) {
        return commissionTrajetRepository.findByDateTrajetBetween(debut, fin)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateTotalCommissionsForPeriod(LocalDateTime debut, LocalDateTime fin) {
        return commissionTrajetRepository.calculateTotalCommissionForPeriod(debut, fin);
    }

    public BigDecimal calculateTotalCommissionsForChauffeur(Long chauffeurId, LocalDateTime debut, LocalDateTime fin) {
        return commissionTrajetRepository.calculateTotalCommissionForChauffeur(chauffeurId, debut, fin);
    }

    @Transactional
    public CommissionTrajetDTO createCommissionTrajet(CommissionTrajetDTO commissionTrajetDTO) {
        // Récupérer la commission à appliquer
        Commission commission = commissionRepository.findById(commissionTrajetDTO.getCommissionId())
                .orElseThrow(() -> new CommissionNotFoundException("Commission non trouvée avec l'ID: " + commissionTrajetDTO.getCommissionId()));

        // Calculer le montant de la commission
        BigDecimal montantCommission = calculateCommissionAmount(commissionTrajetDTO.getMontantTotalTrajet(), commission);
        commissionTrajetDTO.setMontantCommission(montantCommission);

        // Convertir et sauvegarder
        CommissionTrajet commissionTrajet = convertToEntity(commissionTrajetDTO, commission);
        commissionTrajet = commissionTrajetRepository.save(commissionTrajet);
        
        return convertToDTO(commissionTrajet);
    }

    private BigDecimal calculateCommissionAmount(BigDecimal montantTotal, Commission commission) {
        BigDecimal montantCommission = BigDecimal.ZERO;

        switch (commission.getType()) {
            case POURCENTAGE:
                montantCommission = montantTotal.multiply(commission.getTaux().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                break;
            case MONTANT_FIXE:
                montantCommission = commission.getMontantFixe();
                break;
            case MIXTE:
                BigDecimal pourcentage = montantTotal.multiply(commission.getTaux().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                montantCommission = pourcentage.add(commission.getMontantFixe());
                break;
        }

        return montantCommission.setScale(2, RoundingMode.HALF_UP);
    }

    private CommissionTrajetDTO convertToDTO(CommissionTrajet commissionTrajet) {
        CommissionTrajetDTO dto = new CommissionTrajetDTO();
        dto.setId(commissionTrajet.getId());
        dto.setTrajetId(commissionTrajet.getTrajetId());
        dto.setMontantTotalTrajet(commissionTrajet.getMontantTotalTrajet());
        dto.setMontantCommission(commissionTrajet.getMontantCommission());
        dto.setCommissionId(commissionTrajet.getCommission().getId());
        dto.setNomCommission(commissionTrajet.getCommission().getNom());
        dto.setTauxCommission(commissionTrajet.getCommission().getTaux());
        dto.setDateTrajet(commissionTrajet.getDateTrajet());
        dto.setChauffeurId(commissionTrajet.getChauffeurId());
        dto.setPassagerId(commissionTrajet.getPassagerId());
        return dto;
    }

    private CommissionTrajet convertToEntity(CommissionTrajetDTO dto, Commission commission) {
        CommissionTrajet commissionTrajet = new CommissionTrajet();
        commissionTrajet.setId(dto.getId());
        commissionTrajet.setTrajetId(dto.getTrajetId());
        commissionTrajet.setMontantTotalTrajet(dto.getMontantTotalTrajet());
        commissionTrajet.setMontantCommission(dto.getMontantCommission());
        commissionTrajet.setCommission(commission);
        commissionTrajet.setDateTrajet(dto.getDateTrajet());
        commissionTrajet.setChauffeurId(dto.getChauffeurId());
        commissionTrajet.setPassagerId(dto.getPassagerId());
        return commissionTrajet;
    }
}
