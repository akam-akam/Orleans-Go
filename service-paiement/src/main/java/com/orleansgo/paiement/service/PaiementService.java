package com.orleansgo.paiement.service;

import com.orleansgo.paiement.dto.PaiementDTO;
import com.orleansgo.paiement.exception.ResourceNotFoundException;
import com.orleansgo.paiement.mapper.PaiementMapper;
import com.orleansgo.paiement.model.Paiement;
import com.orleansgo.paiement.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    public PaiementDTO creerPaiement(PaiementDTO paiementDTO) {
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        return paiementMapper.toDTO(paiementRepository.save(paiement));
    }

    public List<PaiementDTO> listerTousLesPaiements() {
        return paiementRepository.findAll().stream()
                .map(paiementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaiementDTO trouverParId(UUID id) {
        return paiementRepository.findById(id)
                .map(paiementMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));
    }

    public void supprimerPaiement(UUID id) {
        paiementRepository.deleteById(id);
    }
}
