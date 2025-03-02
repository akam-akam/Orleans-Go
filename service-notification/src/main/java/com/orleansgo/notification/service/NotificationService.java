package com.orleansgo.notification.service;

import com.orleansgo.notification.model.Notification;
import com.orleansgo.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public NotificationService(
            NotificationRepository notificationRepository,
            SimpMessagingTemplate messagingTemplate,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Notification creerNotification(String type, String contenu, UUID utilisateurId) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setContenu(contenu);
        notification.setUtilisateurId(utilisateurId);
        notification.setDateCreation(LocalDateTime.now());
        notification.setLue(false);

        notification = notificationRepository.save(notification);

        // Envoyer la notification via WebSocket
        messagingTemplate.convertAndSendToUser(
                utilisateurId.toString(),
                "/queue/notifications",
                notification
        );

        return notification;
    }

    public void envoyerNotificationKafka(String topic, Map<String, Object> message) {
        kafkaTemplate.send(topic, message);
        logger.info("Notification envoyée au topic Kafka: {}", topic);
    }

    @KafkaListener(topics = "nouvelle-course", groupId = "${spring.kafka.consumer.group-id}")
    public void ecouterNouvelleCourse(Map<String, Object> message) {
        logger.info("Nouvelle course reçue: {}", message);

        if (message.containsKey("chauffeurId") && message.containsKey("passagerId")) {
            // Notifier le chauffeur
            UUID chauffeurId = UUID.fromString(message.get("chauffeurId").toString());
            String adresseDepart = message.get("adresseDepart").toString();

            creerNotification(
                    "NOUVELLE_COURSE",
                    "Nouvelle demande de course à " + adresseDepart,
                    chauffeurId
            );

            // Notifier le passager
            UUID passagerId = UUID.fromString(message.get("passagerId").toString());
            creerNotification(
                    "COURSE_CONFIRMEE",
                    "Votre course a été prise en compte et un chauffeur a été assigné",
                    passagerId
            );
        }
    }

    @KafkaListener(topics = "annulation-course", groupId = "${spring.kafka.consumer.group-id}")
    public void ecouterAnnulationCourse(Map<String, Object> message) {
        logger.info("Annulation de course reçue: {}", message);

        if (message.containsKey("chauffeurId") && message.containsKey("passagerId")) {
            UUID chauffeurId = UUID.fromString(message.get("chauffeurId").toString());
            UUID passagerId = UUID.fromString(message.get("passagerId").toString());
            String raison = message.getOrDefault("raison", "Aucune raison spécifiée").toString();

            // Notifier le chauffeur
            creerNotification(
                    "COURSE_ANNULEE",
                    "Course annulée. Raison: " + raison,
                    chauffeurId
            );

            // Notifier le passager
            creerNotification(
                    "COURSE_ANNULEE",
                    "Votre course a été annulée. Raison: " + raison,
                    passagerId
            );
        }
    }

    public List<Notification> getNotificationsUtilisateur(UUID utilisateurId) {
        return notificationRepository.findByUtilisateurIdOrderByDateCreationDesc(utilisateurId);
    }

    public List<Notification> getNotificationsNonLues(UUID utilisateurId) {
        return notificationRepository.findByUtilisateurIdAndLueFalseOrderByDateCreationDesc(utilisateurId);
    }

    public Notification marquerCommeLue(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setLue(true);
        return notificationRepository.save(notification);
    }

    public void marquerToutesCommeLues(UUID utilisateurId) {
        List<Notification> notifications = notificationRepository.findByUtilisateurIdAndLueFalse(utilisateurId);
        for (Notification notification : notifications) {
            notification.setLue(true);
        }
        notificationRepository.saveAll(notifications);
    }
}