package com.tecsup.petclinic.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para la entidad Specialty (controlador REST).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    public void testFindSpecialtyById() throws Exception {
        mockMvc.perform(get("/api/specialties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("radiology"))
                .andExpect(jsonPath("$.office").value("Farewell"))
                .andExpect(jsonPath("$.openHour").value(8))
                .andExpect(jsonPath("$.closeHour").value(18));
    }

    @Test
    public void testFindSpecialtyById_NotFound() throws Exception {
        mockMvc.perform(get("/api/specialties/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByName() throws Exception {
        mockMvc.perform(get("/api/specialties/name/surgery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("surgery"));
    }

    @Test
    public void testFindByOffice() throws Exception {
        mockMvc.perform(get("/api/specialties/office/Maryland"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("surgery"));
    }

    @Test
    public void testCreateSpecialty() throws Exception {
        SpecialtyDTO dto = SpecialtyDTO.builder()
                .name("oncology")
                .office("Kennedy")
                .openHour(9)
                .closeHour(17)
                .build();

        String response = mockMvc.perform(post("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("oncology"))
                .andExpect(jsonPath("$.office").value("Kennedy"))
                .andReturn().getResponse().getContentAsString();

        log.info("Specialty creada: {}", response);

        // Limpieza: eliminar la especialidad creada para no afectar otras pruebas.
        SpecialtyDTO created = objectMapper.readValue(response, SpecialtyDTO.class);
        mockMvc.perform(delete("/api/specialties/" + created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateSpecialty() throws Exception {
        SpecialtyDTO dto = SpecialtyDTO.builder()
                .name("cardiology")
                .office("Halcyon")
                .openHour(8)
                .closeHour(16)
                .build();

        String createdJson = mockMvc.perform(post("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SpecialtyDTO created = objectMapper.readValue(createdJson, SpecialtyDTO.class);

        created.setName("cardiology-updated");
        created.setOffice("Halcyon 2");

        mockMvc.perform(put("/api/specialties/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("cardiology-updated"))
                .andExpect(jsonPath("$.office").value("Halcyon 2"));

        // Limpieza
        mockMvc.perform(delete("/api/specialties/" + created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteSpecialty() throws Exception {
        SpecialtyDTO dto = SpecialtyDTO.builder()
                .name("dermatology")
                .office("Plaza")
                .openHour(10)
                .closeHour(18)
                .build();

        String createdJson = mockMvc.perform(post("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SpecialtyDTO created = objectMapper.readValue(createdJson, SpecialtyDTO.class);

        mockMvc.perform(delete("/api/specialties/" + created.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/api/specialties/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
