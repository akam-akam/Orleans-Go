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
