
package com.orleansgo.administrateur.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orleansgo.administrateur.dto.AdministrateurDTO;
import com.orleansgo.administrateur.model.RoleAdministrateur;
import com.orleansgo.administrateur.service.AdministrateurService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministrateurController.class)
public class AdministrateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdministrateurService administrateurService;

    private AdministrateurDTO administrateurDTO;

    @BeforeEach
    public void setup() {
        administrateurDTO = new AdministrateurDTO();
        administrateurDTO.setId("1");
        administrateurDTO.setEmail("admin@orleansgo.com");
        administrateurDTO.setNom("Admin");
        administrateurDTO.setPrenom("Test");
        administrateurDTO.setRole(RoleAdministrateur.ADMIN_STANDARD);
        administrateurDTO.setActif(true);
        administrateurDTO.setDateCreation(LocalDateTime.now());
    }

    @Test
    public void testGetAllAdministrateurs() throws Exception {
        List<AdministrateurDTO> admins = Arrays.asList(administrateurDTO);
        
        when(administrateurService.getAllAdministrateurs()).thenReturn(admins);

        mockMvc.perform(get("/api/administrateurs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].email").value("admin@orleansgo.com"));
    }

    @Test
    public void testGetAdministrateur() throws Exception {
        when(administrateurService.getAdministrateur("1")).thenReturn(administrateurDTO);

        mockMvc.perform(get("/api/administrateurs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("admin@orleansgo.com"));
    }

    @Test
    public void testCreateAdministrateur() throws Exception {
        when(administrateurService.createAdministrateur(any(AdministrateurDTO.class), eq("password"))).thenReturn(administrateurDTO);

        mockMvc.perform(post("/api/administrateurs")
                .param("motDePasse", "password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(administrateurDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("admin@orleansgo.com"));
    }

    @Test
    public void testUpdateAdministrateur() throws Exception {
        when(administrateurService.updateAdministrateur(eq("1"), any(AdministrateurDTO.class))).thenReturn(administrateurDTO);

        mockMvc.perform(put("/api/administrateurs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(administrateurDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("admin@orleansgo.com"));
    }

    @Test
    public void testDeleteAdministrateur() throws Exception {
        mockMvc.perform(delete("/api/administrateurs/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testChangeRole() throws Exception {
        when(administrateurService.changeAdministrateurRole("1", RoleAdministrateur.SUPER_ADMIN)).thenReturn(administrateurDTO);

        mockMvc.perform(patch("/api/administrateurs/1/role")
                .param("role", "SUPER_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testChangeStatus() throws Exception {
        when(administrateurService.updateAdministrateurStatus("1", false)).thenReturn(administrateurDTO);

        mockMvc.perform(patch("/api/administrateurs/1/status")
                .param("actif", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }
}
