package com.orleansgo.vehicule.service;

import com.orleansgo.vehicule.dto.VehiculeDTO;
import com.orleansgo.vehicule.exception.ResourceNotFoundException;
import com.orleansgo.vehicule.mapper.VehiculeMapper;
import com.orleansgo.vehicule.model.Vehicule;
import com.orleansgo.vehicule.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    public VehiculeDTO creerVehicule(VehiculeDTO vehiculeDTO) {
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);
        return vehiculeMapper.toDTO(vehiculeRepository.save(vehicule));
    }

    public List<VehiculeDTO> listerTousLesVehicules() {
        return vehiculeRepository.findAll().stream()
                .map(vehiculeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VehiculeDTO trouverParId(UUID id) {
        return vehiculeRepository.findById(id)
                .map(vehiculeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule non trouvé"));
    }

    public void supprimerVehicule(UUID id) {
        vehiculeRepository.deleteById(id);
    }
}
