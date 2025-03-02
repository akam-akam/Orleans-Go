
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
