
package com.orleansgo.notification.service;

import com.orleansgo.notification.dto.EmailNotificationDTO;
import com.orleansgo.notification.dto.NotificationDTO;
import com.orleansgo.notification.dto.SMSNotificationDTO;
import com.orleansgo.notification.model.Notification;
import com.orleansgo.notification.model.NotificationType;
import com.orleansgo.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private SMSService smsService;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private NotificationDTO notificationDTO;
    private EmailNotificationDTO emailDTO;
    private SMSNotificationDTO smsDTO;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setType(NotificationType.EMAIL);
        notification.setTitle("Test Notification");
        notification.setContent("This is a test notification");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setUserId(10L);
        notificationDTO.setType(NotificationType.EMAIL.name());
        notificationDTO.setTitle("Test Notification");
        notificationDTO.setContent("This is a test notification");
        notificationDTO.setRead(false);
        notificationDTO.setCreatedAt(LocalDateTime.now());
        
        emailDTO = new EmailNotificationDTO();
        emailDTO.setToEmail("user@example.com");
        emailDTO.setSubject("Test Email");
        emailDTO.setBody("This is a test email");
        emailDTO.setUserId(10L);
        
        smsDTO = new SMSNotificationDTO();
        smsDTO.setPhoneNumber("+33612345678");
        smsDTO.setMessage("This is a test SMS");
        smsDTO.setUserId(10L);
    }

    @Test
    void getAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(notification));
        
        List<NotificationDTO> result = notificationService.getAllNotifications();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notification.getId(), result.get(0).getId());
    }

    @Test
    void getNotificationById() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        
        NotificationDTO result = notificationService.getNotificationById(1L);
        
        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
    }
    
    @Test
    void getNotificationsByUserId() {
        when(notificationRepository.findByUserId(10L)).thenReturn(Arrays.asList(notification));
        
        List<NotificationDTO> result = notificationService.getNotificationsByUserId(10L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notification.getUserId(), result.get(0).getUserId());
    }
    
    @Test
    void sendEmailNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        
        NotificationDTO result = notificationService.sendEmailNotification(emailDTO);
        
        assertNotNull(result);
        verify(emailService, times(1)).sendEmail(eq(emailDTO.getToEmail()), eq(emailDTO.getSubject()), eq(emailDTO.getBody()));
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
    
    @Test
    void sendSMSNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doNothing().when(smsService).sendSMS(anyString(), anyString());
        
        NotificationDTO result = notificationService.sendSMSNotification(smsDTO);
        
        assertNotNull(result);
        verify(smsService, times(1)).sendSMS(eq(smsDTO.getPhoneNumber()), eq(smsDTO.getMessage()));
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
    
    @Test
    void markNotificationAsRead() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        
        notificationService.markNotificationAsRead(1L);
        
        assertTrue(notification.isRead());
        verify(notificationRepository, times(1)).save(notification);
    }
    
    @Test
    void deleteNotification() {
        doNothing().when(notificationRepository).deleteById(1L);
        
        notificationService.deleteNotification(1L);
        
        verify(notificationRepository, times(1)).deleteById(1L);
    }
}
