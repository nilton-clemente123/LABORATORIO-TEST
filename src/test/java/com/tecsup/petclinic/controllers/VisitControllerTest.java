package com.tecsup.petclinic.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VisitDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Pruebas de integración para la entidad Visit (controlador REST).
 */


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    public void testFindVisitById() throws Exception {
        mockMvc.perform(get("/api/visits/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.vetId").value(1))
                .andExpect(jsonPath("$.visitDate").value("2024-01-15"))
                .andExpect(jsonPath("$.description").value("general checkup"))
                .andExpect(jsonPath("$.cost").value(65.0));
    }

    @Test
    public void testFindVisitById_NotFound() throws Exception {
        mockMvc.perform(get("/api/visits/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByPetId() throws Exception {
        mockMvc.perform(get("/api/visits/pet/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].petId").value(7));
    }

    @Test
    public void testFindByVetId() throws Exception {
        mockMvc.perform(get("/api/visits/vet/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].vetId").value(3));
    }

    @Test
    public void testCreateVisit() throws Exception {
        VisitDTO dto = VisitDTO.builder()
                .petId(1)
                .vetId(1)
                .visitDate(LocalDate.of(2024, 5, 10))
                .description("vaccination")
                .cost(new BigDecimal("30.50"))
                .build();

        String response = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.vetId").value(1))
                .andExpect(jsonPath("$.visitDate").value("2024-05-10"))
                .andExpect(jsonPath("$.description").value("vaccination"))
                .andExpect(jsonPath("$.cost").value(30.5))
                .andReturn().getResponse().getContentAsString();

        log.info("Visit creada: {}", response);

        // Limpieza: eliminar la visita creada para no afectar otras pruebas.
        VisitDTO created = objectMapper.readValue(response, VisitDTO.class);
        mockMvc.perform(delete("/api/visits/" + created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateVisit() throws Exception {
        VisitDTO dto = VisitDTO.builder()
                .petId(1)
                .vetId(1)
                .visitDate(LocalDate.of(2024, 6, 15))
                .description("checkup")
                .cost(new BigDecimal("50.00"))
                .build();

        String createdJson = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VisitDTO created = objectMapper.readValue(createdJson, VisitDTO.class);

        created.setDescription("checkup-updated");
        created.setCost(new BigDecimal("75.25"));

        mockMvc.perform(put("/api/visits/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("checkup-updated"))
                .andExpect(jsonPath("$.cost").value(75.25));

        // Limpieza
        mockMvc.perform(delete("/api/visits/" + created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteVisit() throws Exception {
        VisitDTO dto = VisitDTO.builder()
                .petId(1)
                .vetId(1)
                .visitDate(LocalDate.of(2024, 7, 1))
                .description("to delete")
                .cost(new BigDecimal("10.00"))
                .build();

        String createdJson = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        VisitDTO created = objectMapper.readValue(createdJson, VisitDTO.class);

        mockMvc.perform(delete("/api/visits/" + created.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/api/visits/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
