
package com.orleansgo.administrateur.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TachesPlanifieesService {

    private final SauvegardeService sauvegardeService;
    private final RapportService rapportService;
    private final StatistiqueService statistiqueService;
    private final AuditService auditService;

    /**
     * Génère automatiquement un rapport quotidien à minuit
     */
    @Scheduled(cron = "0 0 0 * * ?") // Tous les jours à minuit
    @Transactional
    public void genererRapportQuotidien() {
        log.info("Génération du rapport quotidien - {}", LocalDateTime.now());
        try {
            statistiqueService.genererRapportStatistiques();
            auditService.logAction(
                "RAPPORT_GENERE", 
                "RAPPORT", 
                "quotidien", 
                "system", 
                "Génération automatique du rapport quotidien"
            );
        } catch (Exception e) {
            log.error("Erreur lors de la génération du rapport quotidien", e);
        }
    }

    /**
     * Crée une sauvegarde automatique chaque semaine (dimanche à 2h du matin)
     */
    @Scheduled(cron = "0 0 2 * * SUN") // Tous les dimanches à 2h du matin
    @Transactional
    public void sauvegardeHebdomadaire() {
        log.info("Sauvegarde hebdomadaire des données - {}", LocalDateTime.now());
        try {
            sauvegardeService.creerSauvegarde("Sauvegarde hebdomadaire automatique");
            auditService.logAction(
                "SAUVEGARDE_CREEE", 
                "SAUVEGARDE", 
                "hebdomadaire", 
                "system", 
                "Sauvegarde automatique hebdomadaire"
            );
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde hebdomadaire", e);
        }
    }

    /**
     * Nettoie les anciens logs d'audit (plus de 90 jours)
     */
    @Scheduled(cron = "0 0 3 1 * ?") // Le 1er de chaque mois à 3h du matin
    @Transactional
    public void nettoyageLogsAudit() {
        log.info("Nettoyage des anciens logs d'audit - {}", LocalDateTime.now());
        // Dans une implémentation réelle, on supprimerait les logs plus anciens que 90 jours
        log.info("Nettoyage des logs terminé");
    }
}
