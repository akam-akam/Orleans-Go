package com.orleansgo.notification.service;

import com.orleansgo.notification.dto.NotificationDTO;
import com.orleansgo.notification.exception.NotificationException;
import com.orleansgo.notification.mapper.NotificationMapper;
import com.orleansgo.notification.model.Notification;
import com.orleansgo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationDTO creerNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.toEntity(notificationDTO);
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    public List<NotificationDTO> listerToutesLesNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO trouverParId(UUID id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDTO)
                .orElseThrow(() -> new NotificationException("Notification non trouvée"));
    }

    public void supprimerNotification(UUID id) {
        notificationRepository.deleteById(id);
    }
}
package com.orleansgo.notification.service;

import com.orleansgo.notification.dto.EmailNotificationDTO;
import com.orleansgo.notification.dto.PushNotificationDTO;
import com.orleansgo.notification.dto.SmsNotificationDTO;
import com.orleansgo.notification.model.Notification;
import com.orleansgo.notification.model.NotificationType;
import com.orleansgo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender emailSender;
    private final RestTemplate restTemplate;

    @Transactional
    public void sendEmailNotification(EmailNotificationDTO emailDTO) {
        // Envoyer l'email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDTO.getDestinataire());
        message.setSubject(emailDTO.getSujet());
        message.setText(emailDTO.getContenu());
        emailSender.send(message);
        
        // Enregistrer la notification
        Notification notification = new Notification();
        notification.setUserId(emailDTO.getUserId());
        notification.setType(NotificationType.EMAIL);
        notification.setTitre(emailDTO.getSujet());
        notification.setContenu(emailDTO.getContenu());
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setLu(false);
        
        notificationRepository.save(notification);
    }
    
    @Transactional
    public void sendSmsNotification(SmsNotificationDTO smsDTO) {
        // Logique d'envoi de SMS (via un service tiers)
        // Exemple avec API SMS
        // restTemplate.postForEntity("URL_API_SMS", smsDTO, String.class);
        
        // Enregistrer la notification
        Notification notification = new Notification();
        notification.setUserId(smsDTO.getUserId());
        notification.setType(NotificationType.SMS);
        notification.setTitre("Notification SMS");
        notification.setContenu(smsDTO.getMessage());
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setLu(false);
        
        notificationRepository.save(notification);
    }
    
    @Transactional
    public void sendPushNotification(PushNotificationDTO pushDTO) {
        // Logique d'envoi de notification push (via Firebase ou autre)
        // restTemplate.postForEntity("URL_API_PUSH", pushDTO, String.class);
        
        // Enregistrer la notification
        Notification notification = new Notification();
        notification.setUserId(pushDTO.getUserId());
        notification.setType(NotificationType.PUSH);
        notification.setTitre(pushDTO.getTitre());
        notification.setContenu(pushDTO.getMessage());
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setLu(false);
        
        notificationRepository.save(notification);
    }
    
    public List<Notification> getUserNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByDateEnvoiDesc(userId);
    }
    
    @Transactional
    public void markNotificationAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setLu(true);
        notification.setDateLecture(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
