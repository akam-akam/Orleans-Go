
package com.orleansgo.evenement.config;

import com.orleansgo.evenement.service.EvenementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

    private final EvenementService evenementService;

    // Exécuté toutes les heures pour mettre à jour le statut des événements
    @Scheduled(fixedRate = 3600000)
    public void updateEventsStatus() {
        evenementService.updateEventsStatus();
    }
}
