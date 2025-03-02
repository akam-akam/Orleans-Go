
package com.orleansgo.conducteur.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orleansgo.conducteur.dto.ConducteurDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConducteurControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllConducteurs_shouldReturnEmptyList_whenNoDriversExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/conducteurs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void createConducteur_shouldReturnConducteur_whenValidInput() throws Exception {
        ConducteurDTO conducteurDTO = new ConducteurDTO();
        conducteurDTO.setUserid("1");
        conducteurDTO.setNumeroPermis("123456789");
        conducteurDTO.setDateExpirationPermis(java.time.LocalDate.now().plusYears(5));
        conducteurDTO.setVerifie(false);
        conducteurDTO.setDisponible(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/conducteurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conducteurDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userid").value("1"))
                .andExpect(jsonPath("$.numeroPermis").value("123456789"));
    }
}
