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
