
package com.orleansgo.commission.service;

import com.orleansgo.commission.dto.CommissionDTO;
import com.orleansgo.commission.exception.CommissionNotFoundException;
import com.orleansgo.commission.model.Commission;
import com.orleansgo.commission.repository.CommissionRepository;
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

    public List<CommissionDTO> getAllCommissions() {
        return commissionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CommissionDTO> getActiveCommissions() {
        return commissionRepository.findActiveCommissions(LocalDateTime.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommissionDTO getCommissionById(Long id) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new CommissionNotFoundException("Commission non trouvée avec l'ID: " + id));
        return convertToDTO(commission);
    }

    @Transactional
    public CommissionDTO createCommission(CommissionDTO commissionDTO) {
        Commission commission = convertToEntity(commissionDTO);
        commission = commissionRepository.save(commission);
        return convertToDTO(commission);
    }

    @Transactional
    public CommissionDTO updateCommission(Long id, CommissionDTO commissionDTO) {
        Commission existingCommission = commissionRepository.findById(id)
                .orElseThrow(() -> new CommissionNotFoundException("Commission non trouvée avec l'ID: " + id));

        existingCommission.setNom(commissionDTO.getNom());
        existingCommission.setDescription(commissionDTO.getDescription());
        existingCommission.setTaux(commissionDTO.getTaux());
        existingCommission.setMontantFixe(commissionDTO.getMontantFixe());
        existingCommission.setType(commissionDTO.getType());
        existingCommission.setDateDebut(commissionDTO.getDateDebut());
        existingCommission.setDateFin(commissionDTO.getDateFin());
        existingCommission.setActive(commissionDTO.isActive());

        existingCommission = commissionRepository.save(existingCommission);
        return convertToDTO(existingCommission);
    }

    @Transactional
    public void deleteCommission(Long id) {
        if (!commissionRepository.existsById(id)) {
            throw new CommissionNotFoundException("Commission non trouvée avec l'ID: " + id);
        }
        commissionRepository.deleteById(id);
    }

    @Transactional
    public CommissionDTO activateCommission(Long id) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new CommissionNotFoundException("Commission non trouvée avec l'ID: " + id));
        commission.setActive(true);
        commission = commissionRepository.save(commission);
        return convertToDTO(commission);
    }

    @Transactional
    public CommissionDTO deactivateCommission(Long id) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new CommissionNotFoundException("Commission non trouvée avec l'ID: " + id));
        commission.setActive(false);
        commission = commissionRepository.save(commission);
        return convertToDTO(commission);
    }

    private CommissionDTO convertToDTO(Commission commission) {
        CommissionDTO dto = new CommissionDTO();
        dto.setId(commission.getId());
        dto.setNom(commission.getNom());
        dto.setDescription(commission.getDescription());
        dto.setTaux(commission.getTaux());
        dto.setMontantFixe(commission.getMontantFixe());
        dto.setType(commission.getType());
        dto.setDateDebut(commission.getDateDebut());
        dto.setDateFin(commission.getDateFin());
        dto.setActive(commission.isActive());
        return dto;
    }

    private Commission convertToEntity(CommissionDTO dto) {
        Commission commission = new Commission();
        commission.setId(dto.getId());
        commission.setNom(dto.getNom());
        commission.setDescription(dto.getDescription());
        commission.setTaux(dto.getTaux());
        commission.setMontantFixe(dto.getMontantFixe());
        commission.setType(dto.getType());
        commission.setDateDebut(dto.getDateDebut());
        commission.setDateFin(dto.getDateFin());
        commission.setActive(dto.isActive());
        return commission;
    }
}
