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
package com.orleansgo.vehicule.service;

import com.orleansgo.vehicule.dto.VehiculeDTO;
import com.orleansgo.vehicule.model.StatutVehicule;
import com.orleansgo.vehicule.model.TypeVehicule;
import com.orleansgo.vehicule.model.Vehicule;
import com.orleansgo.vehicule.repository.VehiculeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculeService {
    
    @Autowired
    private VehiculeRepository vehiculeRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public List<VehiculeDTO> getAllVehicules() {
        return vehiculeRepository.findAll().stream()
                .map(vehicule -> modelMapper.map(vehicule, VehiculeDTO.class))
                .collect(Collectors.toList());
    }
    
    public VehiculeDTO getVehiculeById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec l'ID : " + id));
        return modelMapper.map(vehicule, VehiculeDTO.class);
    }
    
    public List<VehiculeDTO> getVehiculesByChauffeur(Long chauffeurId) {
        return vehiculeRepository.findByChauffeurId(chauffeurId).stream()
                .map(vehicule -> modelMapper.map(vehicule, VehiculeDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<VehiculeDTO> getVehiculesByType(TypeVehicule typeVehicule) {
        return vehiculeRepository.findByTypeVehicule(typeVehicule).stream()
                .map(vehicule -> modelMapper.map(vehicule, VehiculeDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<VehiculeDTO> getVehiculesByStatut(StatutVehicule statut) {
        return vehiculeRepository.findByStatut(statut).stream()
                .map(vehicule -> modelMapper.map(vehicule, VehiculeDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<VehiculeDTO> getVehiculesByTypeAndStatut(TypeVehicule typeVehicule, StatutVehicule statut) {
        return vehiculeRepository.findByTypeVehiculeAndStatut(typeVehicule, statut).stream()
                .map(vehicule -> modelMapper.map(vehicule, VehiculeDTO.class))
                .collect(Collectors.toList());
    }
    
    public VehiculeDTO createVehicule(VehiculeDTO vehiculeDTO) {
        Vehicule vehicule = modelMapper.map(vehiculeDTO, Vehicule.class);
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);
        return modelMapper.map(savedVehicule, VehiculeDTO.class);
    }
    
    public VehiculeDTO updateVehicule(Long id, VehiculeDTO vehiculeDTO) {
        if (!vehiculeRepository.existsById(id)) {
            throw new RuntimeException("Véhicule non trouvé avec l'ID : " + id);
        }
        
        Vehicule vehicule = modelMapper.map(vehiculeDTO, Vehicule.class);
        vehicule.setId(id);
        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);
        return modelMapper.map(updatedVehicule, VehiculeDTO.class);
    }
    
    public void deleteVehicule(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new RuntimeException("Véhicule non trouvé avec l'ID : " + id);
        }
        
        vehiculeRepository.deleteById(id);
    }
    
    public VehiculeDTO changerStatutVehicule(Long id, StatutVehicule nouveauStatut) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec l'ID : " + id));
        
        vehicule.setStatut(nouveauStatut);
        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);
        return modelMapper.map(updatedVehicule, VehiculeDTO.class);
    }
}
