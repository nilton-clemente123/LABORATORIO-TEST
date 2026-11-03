package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.TypeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTypes() throws Exception {
        mockMvc.perform(get("/api/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testGetTypeById() throws Exception {
        mockMvc.perform(get("/api/types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    void testCreateType() throws Exception {
        TypeDTO dto = TypeDTO.builder()
                .name("iguana-" + System.currentTimeMillis())
                .description("Reptile")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(10)
                .careLevel("medium")
                .build();

        mockMvc.perform(post("/api/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(dto.getName()));
    }

    @Test
    void testDeleteType() throws Exception {
        TypeDTO dto = TypeDTO.builder()
                .name("to-delete-" + System.currentTimeMillis())
                .description("Temp type")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(5)
                .careLevel("low")
                .build();

        MvcResult result = mockMvc.perform(post("/api/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        TypeDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), TypeDTO.class);

        mockMvc.perform(delete("/api/types/" + created.getId()))
                .andExpect(status().isNoContent());
    }
}
