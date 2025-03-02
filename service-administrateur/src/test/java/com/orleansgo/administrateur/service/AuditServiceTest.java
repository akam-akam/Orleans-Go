
package com.orleansgo.administrateur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.orleansgo.administrateur.model.AuditLog;
import com.orleansgo.administrateur.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditService auditService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogAction() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(auditLogRepository.save(any(AuditLog.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        auditService.logAction("CREATE", "USER", "123", "admin", "User created");

        // Assert
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    public void testGetAuditLogsByUser() {
        // Arrange
        LocalDateTime debut = LocalDateTime.now().minusDays(7);
        LocalDateTime fin = LocalDateTime.now();
        
        AuditLog log1 = new AuditLog();
        AuditLog log2 = new AuditLog();
        List<AuditLog> expectedLogs = Arrays.asList(log1, log2);
        
        when(auditLogRepository.findByUsernameAndTimestampBetween("admin", debut, fin)).thenReturn(expectedLogs);

        // Act
        List<AuditLog> result = auditService.getAuditLogsByUser("admin", debut, fin);

        // Assert
        assertEquals(expectedLogs, result);
        verify(auditLogRepository, times(1)).findByUsernameAndTimestampBetween("admin", debut, fin);
    }

    @Test
    public void testGetAuditLogsByEntity() {
        // Arrange
        AuditLog log1 = new AuditLog();
        AuditLog log2 = new AuditLog();
        List<AuditLog> expectedLogs = Arrays.asList(log1, log2);
        
        when(auditLogRepository.findByEntityTypeAndEntityId("USER", "123")).thenReturn(expectedLogs);

        // Act
        List<AuditLog> result = auditService.getAuditLogsByEntity("USER", "123");

        // Assert
        assertEquals(expectedLogs, result);
        verify(auditLogRepository, times(1)).findByEntityTypeAndEntityId("USER", "123");
    }

    @Test
    public void testGetAuditLogsByAction() {
        // Arrange
        AuditLog log1 = new AuditLog();
        AuditLog log2 = new AuditLog();
        List<AuditLog> expectedLogs = Arrays.asList(log1, log2);
        
        when(auditLogRepository.findByActionType("CREATE")).thenReturn(expectedLogs);

        // Act
        List<AuditLog> result = auditService.getAuditLogsByAction("CREATE");

        // Assert
        assertEquals(expectedLogs, result);
        verify(auditLogRepository, times(1)).findByActionType("CREATE");
    }
}
