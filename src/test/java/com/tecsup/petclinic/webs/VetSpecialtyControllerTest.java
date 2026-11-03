package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VetSpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllVetSpecialties() throws Exception {
        mockMvc.perform(get("/api/vet-specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testGetVetSpecialtyById() throws Exception {
        // seeded: vetId=2, specialtyId=1
        mockMvc.perform(get("/api/vet-specialties/2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vetId").value(2))
                .andExpect(jsonPath("$.specialtyId").value(1));
    }

    @Test
    void testFindByVetId() throws Exception {
        mockMvc.perform(get("/api/vet-specialties/vet/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testCreateVetSpecialty() throws Exception {
        VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
                .vetId(1)
                .specialtyId(1)
                .certificationDate(LocalDate.of(2020, 1, 1))
                .yearsExperience(5)
                .isPrimary(false)
                .notes("integration test")
                .build();

        mockMvc.perform(post("/api/vet-specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vetId").value(1))
                .andExpect(jsonPath("$.specialtyId").value(1));
    }

    @Test
    void testDeleteVetSpecialty() throws Exception {
        VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
                .vetId(1)
                .specialtyId(2)
                .certificationDate(LocalDate.of(2021, 5, 10))
                .yearsExperience(3)
                .isPrimary(false)
                .notes("to delete")
                .build();

        MvcResult result = mockMvc.perform(post("/api/vet-specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        VetSpecialtyDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), VetSpecialtyDTO.class);

        mockMvc.perform(delete("/api/vet-specialties/" + created.getVetId() + "/" + created.getSpecialtyId()))
                .andExpect(status().isNoContent());
    }
}
