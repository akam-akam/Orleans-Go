package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.AdministrateurDTO;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.mapper.AdministrateurMapper;
import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.repository.AdministrateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrateurService {

    private final AdministrateurRepository administrateurRepository;
    private final AdministrateurMapper administrateurMapper;

    public AdministrateurDTO creerAdministrateur(AdministrateurDTO administrateurDTO) {
        Administrateur administrateur = administrateurMapper.toEntity(administrateurDTO);
        return administrateurMapper.toDTO(administrateurRepository.save(administrateur));
    }

    public List<AdministrateurDTO> listerTousLesAdministrateurs() {
        return administrateurRepository.findAll().stream()
                .map(administrateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AdministrateurDTO trouverParId(UUID id) {
        return administrateurRepository.findById(id)
                .map(administrateurMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Administrateur non trouvé"));
    }

    public void supprimerAdministrateur(UUID id) {
        administrateurRepository.deleteById(id);
    }
}
