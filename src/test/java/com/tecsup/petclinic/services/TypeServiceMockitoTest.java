package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TypeServiceMockitoTest {

    @Autowired
    private TypeService typeService;

    @Autowired
    private TypeMapper typeMapper;

    @MockitoBean
    private TypeRepository repository;

    // ------------------------------------------------------------------
    // Integrante A — CRUD básico
    // ------------------------------------------------------------------

    @Test
    public void testCreateType() {

        Type newType = new Type(null, "ferret", "Domestic mustelid", true, "small", 7, "medium");
        Type newTypeCreated = new Type(1000, "ferret", "Domestic mustelid", true, "small", 7, "medium");

        TypeDTO newTypeDTO = this.typeMapper.mapToDto(newType);
        TypeDTO hopeTypeDTOCreated = this.typeMapper.mapToDto(newTypeCreated);

        Mockito.when(this.repository.save(newType))
                .thenReturn(newTypeCreated);

        TypeDTO newTypeDTOCreated = this.typeService.create(newTypeDTO);

        log.info("Type created : {}", newTypeDTOCreated);

        assertNotNull(newTypeDTOCreated.getId());
        assertEquals(hopeTypeDTOCreated.getId(), newTypeDTOCreated.getId());
        assertEquals(hopeTypeDTOCreated.getName(), newTypeDTOCreated.getName());
        assertEquals(hopeTypeDTOCreated.getDescription(), newTypeDTOCreated.getDescription());
        assertEquals(hopeTypeDTOCreated.getActive(), newTypeDTOCreated.getActive());
        assertEquals(hopeTypeDTOCreated.getSizeCategory(), newTypeDTOCreated.getSizeCategory());
        assertEquals(hopeTypeDTOCreated.getAverageLifespan(), newTypeDTOCreated.getAverageLifespan());
        assertEquals(hopeTypeDTOCreated.getCareLevel(), newTypeDTOCreated.getCareLevel());

        Mockito.verify(this.repository, Mockito.times(1)).save(newType);
    }

    @Test
    public void testUpdateType() {

        String UP_NAME = "guinea pig";
        String UP_DESCRIPTION = "Domestic cavy";
        int UP_LIFESPAN = 6;
        String UP_CARE_LEVEL = "low";

        Type newType = new Type(null, "chinchilla", "Small rodent", true, "small", 15, "high");
        Type newTypeCreate = new Type(4000, "chinchilla", "Small rodent", true, "small", 15, "high");

        TypeDTO newTypeDTO = this.typeMapper.mapToDto(newType);

        // ------------ Create ---------------

        Mockito.when(this.repository.save(newType))
                .thenReturn(newTypeCreate);

        TypeDTO newTypeDTOCreate = this.typeService.create(newTypeDTO);
        log.info("{}", newTypeDTOCreate);

        // ------------ Update ---------------

        newTypeDTOCreate.setName(UP_NAME);
        newTypeDTOCreate.setDescription(UP_DESCRIPTION);
        newTypeDTOCreate.setAverageLifespan(UP_LIFESPAN);
        newTypeDTOCreate.setCareLevel(UP_CARE_LEVEL);

        Type newTypeUpdate = this.typeMapper.mapToEntity(newTypeDTOCreate);

        Mockito.when(this.repository.save(newTypeUpdate))
                .thenReturn(newTypeUpdate);

        TypeDTO typeDTOUpdate = this.typeService.update(newTypeDTOCreate);
        log.info("{}", typeDTOUpdate);

        //            EXPECTED         ACTUAL
        assertEquals(4000, typeDTOUpdate.getId());
        assertEquals(UP_NAME, typeDTOUpdate.getName());
        assertEquals(UP_DESCRIPTION, typeDTOUpdate.getDescription());
        assertEquals(UP_LIFESPAN, typeDTOUpdate.getAverageLifespan());
        assertEquals(UP_CARE_LEVEL, typeDTOUpdate.getCareLevel());
    }

    @Test
    public void testFindTypeById() {

        Type typeExpected = new Type(1, "cat", "Domestic feline", true, "small", 15, "medium");

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(typeExpected));

        TypeDTO type = null;

        try {
            type = this.typeService.findById(1);
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("{}", typeExpected);
        log.info("{}", type);

        assertEquals(typeExpected.getId(), type.getId());
        assertEquals(typeExpected.getName(), type.getName());
        assertEquals(typeExpected.getDescription(), type.getDescription());
        assertEquals(typeExpected.getActive(), type.getActive());
        assertEquals(typeExpected.getSizeCategory(), type.getSizeCategory());
        assertEquals(typeExpected.getAverageLifespan(), type.getAverageLifespan());
        assertEquals(typeExpected.getCareLevel(), type.getCareLevel());

        Mockito.verify(this.repository, Mockito.times(1)).findById(1);
    }

}