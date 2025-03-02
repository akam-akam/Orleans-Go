
package com.orleansgo.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orleansgo.notification.dto.EmailNotificationDTO;
import com.orleansgo.notification.dto.NotificationDTO;
import com.orleansgo.notification.dto.SMSNotificationDTO;
import com.orleansgo.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private NotificationDTO notificationDTO;
    private EmailNotificationDTO emailDTO;
    private SMSNotificationDTO smsDTO;

    @BeforeEach
    void setUp() {
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setUserId(10L);
        notificationDTO.setType("EMAIL");
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
    void getAllNotifications() throws Exception {
        List<NotificationDTO> notifications = Arrays.asList(notificationDTO);
        when(notificationService.getAllNotifications()).thenReturn(notifications);

        mockMvc.perform(get("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(10)))
                .andExpect(jsonPath("$[0].title", is("Test Notification")));
    }

    @Test
    void getNotificationById() throws Exception {
        when(notificationService.getNotificationById(1L)).thenReturn(notificationDTO);

        mockMvc.perform(get("/api/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(10)))
                .andExpect(jsonPath("$.title", is("Test Notification")));
    }

    @Test
    void getNotificationsByUserId() throws Exception {
        List<NotificationDTO> notifications = Arrays.asList(notificationDTO);
        when(notificationService.getNotificationsByUserId(10L)).thenReturn(notifications);

        mockMvc.perform(get("/api/notifications/user/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(10)));
    }

    @Test
    void sendEmailNotification() throws Exception {
        when(notificationService.sendEmailNotification(any(EmailNotificationDTO.class))).thenReturn(notificationDTO);

        mockMvc.perform(post("/api/notifications/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(10)))
                .andExpect(jsonPath("$.title", is("Test Notification")));
    }

    @Test
    void sendSMSNotification() throws Exception {
        when(notificationService.sendSMSNotification(any(SMSNotificationDTO.class))).thenReturn(notificationDTO);

        mockMvc.perform(post("/api/notifications/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(smsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(10)))
                .andExpect(jsonPath("$.title", is("Test Notification")));
    }

    @Test
    void markNotificationAsRead() throws Exception {
        doNothing().when(notificationService).markNotificationAsRead(anyLong());

        mockMvc.perform(put("/api/notifications/1/read")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNotification() throws Exception {
        doNothing().when(notificationService).deleteNotification(anyLong());

        mockMvc.perform(delete("/api/notifications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
