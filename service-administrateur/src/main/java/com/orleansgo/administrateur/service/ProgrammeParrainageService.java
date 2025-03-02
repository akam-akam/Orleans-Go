
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.model.Administrateur;
import com.orleansgo.administrateur.model.ProgrammeParrainage;
import com.orleansgo.administrateur.repository.ProgrammeParrainageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgrammeParrainageService {
    private final ProgrammeParrainageRepository programmeParrainageRepository;
    
    public List<ProgrammeParrainage> findAllProgrammes() {
        return programmeParrainageRepository.findAll();
    }
    
    public Optional<ProgrammeParrainage> findProgrammeById(Long id) {
        return programmeParrainageRepository.findById(id);
    }
    
    public Optional<ProgrammeParrainage> findCurrentProgramme() {
        return programmeParrainageRepository.findCurrentProgramme();
    }
    
    @Transactional
    public ProgrammeParrainage createProgramme(ProgrammeParrainage programme, Administrateur administrateur) {
        programme.setModifiePar(administrateur);
        return programmeParrainageRepository.save(programme);
    }
    
    @Transactional
    public Optional<ProgrammeParrainage> updateProgramme(Long id, ProgrammeParrainage programmeDetails, Administrateur administrateur) {
        return programmeParrainageRepository.findById(id)
                .map(programme -> {
                    programme.setBonusParrain(programmeDetails.getBonusParrain());
                    programme.setBonusFilleul(programmeDetails.getBonusFilleul());
                    programme.setDescription(programmeDetails.getDescription());
                    programme.setActif(programmeDetails.isActif());
                    programme.setModifiePar(administrateur);
                    programme.setDateModification(LocalDateTime.now());
                    
                    return programmeParrainageRepository.save(programme);
                });
    }
    
    @Transactional
    public Optional<ProgrammeParrainage> clotureProgramme(Long id, Administrateur administrateur) {
        return programmeParrainageRepository.findById(id)
                .map(programme -> {
                    if (programme.getDateFin() != null) {
                        throw new RuntimeException("Ce programme a déjà été clôturé");
                    }
                    
                    programme.setDateFin(LocalDateTime.now());
                    programme.setActif(false);
                    programme.setModifiePar(administrateur);
                    programme.setDateModification(LocalDateTime.now());
                    
                    return programmeParrainageRepository.save(programme);
                });
    }
    
    @Transactional
    public ProgrammeParrainage configurerParrainage(BigDecimal bonusParrain, BigDecimal bonusFilleul, Administrateur administrateur) {
        // On désactive le programme actuel si il existe
        Optional<ProgrammeParrainage> programmeActuel = findCurrentProgramme();
        if (programmeActuel.isPresent()) {
            clotureProgramme(programmeActuel.get().getId(), administrateur);
        }
        
        // On crée un nouveau programme
        ProgrammeParrainage nouveauProgramme = new ProgrammeParrainage();
        nouveauProgramme.setBonusParrain(bonusParrain);
        nouveauProgramme.setBonusFilleul(bonusFilleul);
        nouveauProgramme.setDateDebut(LocalDateTime.now());
        nouveauProgramme.setDescription("Nouveau programme de parrainage");
        nouveauProgramme.setActif(true);
        nouveauProgramme.setModifiePar(administrateur);
        
        return programmeParrainageRepository.save(nouveauProgramme);
    }
}
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.ProgrammeParrainageDTO;
import com.orleansgo.administrateur.exception.ResourceNotFoundException;
import com.orleansgo.administrateur.model.ProgrammeParrainage;
import com.orleansgo.administrateur.repository.ProgrammeParrainageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgrammeParrainageService {

    private final ProgrammeParrainageRepository programmeParrainageRepository;

    public List<ProgrammeParrainageDTO> getAllProgrammes() {
        return programmeParrainageRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProgrammeParrainageDTO getProgrammeById(UUID id) {
        ProgrammeParrainage programme = programmeParrainageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programme de parrainage non trouvé avec l'ID: " + id));
        return mapToDTO(programme);
    }

    public ProgrammeParrainageDTO getCurrentProgramme() {
        ProgrammeParrainage programme = programmeParrainageRepository.findByActifTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Aucun programme de parrainage actif trouvé"));
        return mapToDTO(programme);
    }

    @Transactional
    public ProgrammeParrainageDTO createProgramme(ProgrammeParrainageDTO programmeDTO) {
        // Désactiver tous les programmes actifs si on crée un nouveau programme actif
        if (programmeDTO.isActif()) {
            programmeParrainageRepository.findByActifTrue()
                    .ifPresent(programme -> {
                        programme.setActif(false);
                        programme.setDateFin(LocalDateTime.now());
                        programmeParrainageRepository.save(programme);
                    });
        }

        ProgrammeParrainage programme = ProgrammeParrainage.builder()
                .nom(programmeDTO.getNom())
                .description(programmeDTO.getDescription())
                .bonusParrain(programmeDTO.getBonusParrain())
                .bonusFilleul(programmeDTO.getBonusFilleul())
                .nombreCoursesRequises(programmeDTO.getNombreCoursesRequises())
                .actif(programmeDTO.isActif())
                .dateDebut(LocalDateTime.now())
                .build();
        
        ProgrammeParrainage savedProgramme = programmeParrainageRepository.save(programme);
        return mapToDTO(savedProgramme);
    }

    @Transactional
    public ProgrammeParrainageDTO updateProgramme(UUID id, ProgrammeParrainageDTO programmeDTO) {
        ProgrammeParrainage programme = programmeParrainageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programme de parrainage non trouvé avec l'ID: " + id));
        
        // Si on active un programme, désactiver les autres
        if (!programme.isActif() && programmeDTO.isActif()) {
            programmeParrainageRepository.findByActifTrue()
                    .ifPresent(activeProgramme -> {
                        activeProgramme.setActif(false);
                        activeProgramme.setDateFin(LocalDateTime.now());
                        programmeParrainageRepository.save(activeProgramme);
                    });
        }
        
        programme.setNom(programmeDTO.getNom());
        programme.setDescription(programmeDTO.getDescription());
        programme.setBonusParrain(programmeDTO.getBonusParrain());
        programme.setBonusFilleul(programmeDTO.getBonusFilleul());
        programme.setNombreCoursesRequises(programmeDTO.getNombreCoursesRequises());
        programme.setActif(programmeDTO.isActif());
        
        ProgrammeParrainage updatedProgramme = programmeParrainageRepository.save(programme);
        return mapToDTO(updatedProgramme);
    }

    @Transactional
    public ProgrammeParrainageDTO cloturerProgramme(UUID id) {
        ProgrammeParrainage programme = programmeParrainageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programme de parrainage non trouvé avec l'ID: " + id));
        
        programme.setActif(false);
        programme.setDateFin(LocalDateTime.now());
        
        ProgrammeParrainage updatedProgramme = programmeParrainageRepository.save(programme);
        return mapToDTO(updatedProgramme);
    }

    private ProgrammeParrainageDTO mapToDTO(ProgrammeParrainage programme) {
        return ProgrammeParrainageDTO.builder()
                .id(programme.getId())
                .nom(programme.getNom())
                .description(programme.getDescription())
                .bonusParrain(programme.getBonusParrain())
                .bonusFilleul(programme.getBonusFilleul())
                .nombreCoursesRequises(programme.getNombreCoursesRequises())
                .actif(programme.isActif())
                .dateDebut(programme.getDateDebut())
                .dateFin(programme.getDateFin())
                .build();
    }
}
