package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
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

    @MockitoBean
    private PetRepository petRepository;

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

    // ------------------------------------------------------------------
    // Integrante B — Reporte de mascotas por tipo
    // ------------------------------------------------------------------

    @Test
    public void testGetPetCountByType() {

        Type cat = new Type(1, "cat", "Domestic feline", true, "small", 15, "medium");
        Type dog = new Type(2, "dog", "Domestic canine", true, "medium", 12, "medium");
        Type snake = new Type(6, "snake", "Serpent", false, "medium", 20, "high");

        List<Type> types = List.of(cat, dog, snake);

        // 2 cats, 1 dog, 1 snake
        List<Pet> pets = List.of(
                new Pet(1, "Leo", 1, 1, null),
                new Pet(7, "Samantha", 1, 6, null),
                new Pet(3, "Rosy", 2, 3, null),
                new Pet(2, "Basil", 6, 2, null)
        );

        Mockito.when(this.repository.findAll()).thenReturn(types);
        Mockito.when(this.petRepository.findAll()).thenReturn(pets);

        List<PetCountByTypeDTO> report = this.typeService.getPetCountByType(false);

        assertEquals(3, report.size());

        PetCountByTypeDTO catReport = report.stream()
                .filter(dto -> "cat".equals(dto.getTypeName()))
                .findFirst().orElseThrow();
        assertEquals(2L, catReport.getPetCount());

        PetCountByTypeDTO dogReport = report.stream()
                .filter(dto -> "dog".equals(dto.getTypeName()))
                .findFirst().orElseThrow();
        assertEquals(1L, dogReport.getPetCount());

        PetCountByTypeDTO snakeReport = report.stream()
                .filter(dto -> "snake".equals(dto.getTypeName()))
                .findFirst().orElseThrow();
        assertEquals(1L, snakeReport.getPetCount());
    }

    @Test
    public void testGetPetCountByType_Empty() {

        Mockito.when(this.repository.findAll()).thenReturn(List.of());
        Mockito.when(this.petRepository.findAll()).thenReturn(List.of());

        List<PetCountByTypeDTO> report = this.typeService.getPetCountByType(false);

        assertNotNull(report);
        assertTrue(report.isEmpty());
    }

    @Test
    public void testDeleteType() {

        Type rabbit = new Type(7, "rabbit", "Domestic rabbit", true, "small", 9, "medium");

        Mockito.when(this.repository.findById(7)).thenReturn(Optional.of(rabbit));
        Mockito.doNothing().when(this.repository).delete(rabbit);

        try {
            this.typeService.delete(7);
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }

        Mockito.verify(this.repository, Mockito.times(1)).delete(rabbit);
    }

    // ------------------------------------------------------------------
    // Integrante C — Filtros y errores
    // ------------------------------------------------------------------

    @Test
    public void testGetPetCountByType_ExcludeInactive() {

        Type cat = new Type(1, "cat", "Domestic feline", true, "small", 15, "medium");
        Type dog = new Type(2, "dog", "Domestic canine", true, "medium", 12, "medium");
        Type snake = new Type(6, "snake", "Serpent", false, "medium", 20, "high"); // Inactivo (active = false)

        List<Type> types = List.of(cat, dog, snake);

        List<Pet> pets = List.of(
                new Pet(1, "Leo", 1, 1, null),
                new Pet(7, "Samantha", 1, 6, null),
                new Pet(3, "Rosy", 2, 3, null),
                new Pet(2, "Basil", 6, 2, null)
        );

        Mockito.when(this.repository.findAll()).thenReturn(types);
        Mockito.when(this.petRepository.findAll()).thenReturn(pets);

        // Se envía true para excluir tipos inactivos
        List<PetCountByTypeDTO> report = this.typeService.getPetCountByType(true);

        log.info("Reporte excluyendo inactivos: {}", report);

        // Debe incluir solo los 2 activos (cat y dog) y excluir a snake
        assertEquals(2, report.size());

        boolean containsSnake = report.stream()
                .anyMatch(dto -> "snake".equals(dto.getTypeName()));
        assertFalse(containsSnake, "El reporte no debe incluir la serpiente (active = false)");
    }

    @Test
    public void testFindTypeById_NotFound() {

        int ID_INEXISTENTE = 999;

        Mockito.when(this.repository.findById(ID_INEXISTENTE))
                .thenReturn(Optional.empty());

        try {
            this.typeService.findById(ID_INEXISTENTE);
            fail("Debería haber lanzado la excepción TypeNotFoundException");
        } catch (TypeNotFoundException e) {
            log.info("Excepción capturada correctamente: {}", e.getMessage());
            assertTrue(true);
        }

        Mockito.verify(this.repository, Mockito.times(1)).findById(ID_INEXISTENTE);
    }

    @Test
    public void testDeleteType_NotFound() {

        int ID_INEXISTENTE = 999;

        Mockito.when(this.repository.findById(ID_INEXISTENTE))
                .thenReturn(Optional.empty());

        try {
            this.typeService.delete(ID_INEXISTENTE);
            fail("Debería haber lanzado la excepción TypeNotFoundException");
        } catch (TypeNotFoundException e) {
            log.info("Excepción al eliminar capturada correctamente: {}", e.getMessage());
            assertTrue(true);
        }

        Mockito.verify(this.repository, Mockito.times(1)).findById(ID_INEXISTENTE);
        Mockito.verify(this.repository, Mockito.never()).delete(Mockito.any(Type.class));
    }

}