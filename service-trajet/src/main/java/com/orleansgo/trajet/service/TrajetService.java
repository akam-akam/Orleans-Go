package com.orleansgo.trajet.service;

import com.orleansgo.trajet.dto.TrajetDTO;
import com.orleansgo.trajet.exception.ResourceNotFoundException;
import com.orleansgo.trajet.mapper.TrajetMapper;
import com.orleansgo.trajet.model.Trajet;
import com.orleansgo.trajet.repository.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetService {

    private final TrajetRepository trajetRepository;
    private final TrajetMapper trajetMapper;

    public TrajetDTO creerTrajet(TrajetDTO trajetDTO) {
        Trajet trajet = trajetMapper.toEntity(trajetDTO);
        return trajetMapper.toDTO(trajetRepository.save(trajet));
    }

    public List<TrajetDTO> listerTousLesTrajets() {
        return trajetRepository.findAll().stream()
                .map(trajetMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrajetDTO trouverParId(UUID id) {
        return trajetRepository.findById(id)
                .map(trajetMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet non trouvé"));
    }

    public void supprimerTrajet(UUID id) {
        trajetRepository.deleteById(id);
    }
}
