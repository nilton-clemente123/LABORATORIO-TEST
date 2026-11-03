package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.TypeRepository;
import com.tecsup.petclinic.services.TypeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class TypeServiceTest {

    @Autowired
    private TypeService typeService;

    /**
     *
     */
    @Test
    public void testFindTypeById() {

        final String NAME_EXPECTED = "cat";

        Integer ID = 1;

        TypeDTO type = null;

        try {
            type = this.typeService.findById(ID);
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }
        assertEquals(NAME_EXPECTED, type.getName());
    }

    /**
     *
     */
    @Test
    public void testCreateType() {

        String TYPE_NAME = "ferret";
        String DESCRIPTION = "Domestic mustelid";
        String SIZE_CATEGORY = "small";
        int LIFESPAN = 7;
        String CARE_LEVEL = "medium";

        TypeDTO typeDTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description(DESCRIPTION)
                .active(true)
                .sizeCategory(SIZE_CATEGORY)
                .averageLifespan(LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        TypeDTO newTypeDTO = this.typeService.create(typeDTO);

        log.info("TYPE CREATED :" + newTypeDTO.toString());

        assertNotNull(newTypeDTO.getId());
        assertEquals(TYPE_NAME, newTypeDTO.getName());
        assertEquals(DESCRIPTION, newTypeDTO.getDescription());
        assertEquals(SIZE_CATEGORY, newTypeDTO.getSizeCategory());
        assertEquals(LIFESPAN, newTypeDTO.getAverageLifespan());
        assertEquals(CARE_LEVEL, newTypeDTO.getCareLevel());
    }

    /**
     *
     */
    @Test
    public void testUpdateType() {

        String TYPE_NAME = "chinchilla";
        String DESCRIPTION = "Small rodent";
        int LIFESPAN = 15;
        String CARE_LEVEL = "high";

        String UP_TYPE_NAME = "guinea pig";
        String UP_DESCRIPTION = "Domestic cavy";
        int UP_LIFESPAN = 6;
        String UP_CARE_LEVEL = "low";

        TypeDTO typeDTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description(DESCRIPTION)
                .active(true)
                .sizeCategory("small")
                .averageLifespan(LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        // ------------ Create ---------------

        log.info(">" + typeDTO);
        TypeDTO typeDTOCreated = this.typeService.create(typeDTO);
        log.info(">>" + typeDTOCreated);

        // ------------ Update ---------------

        // Prepare data for update
        typeDTOCreated.setName(UP_TYPE_NAME);
        typeDTOCreated.setDescription(UP_DESCRIPTION);
        typeDTOCreated.setAverageLifespan(UP_LIFESPAN);
        typeDTOCreated.setCareLevel(UP_CARE_LEVEL);

        // Execute update
        TypeDTO upgradeTypeDTO = this.typeService.update(typeDTOCreated);
        log.info(">>>>" + upgradeTypeDTO);

        //            EXPECTED          ACTUAL
        assertEquals(UP_TYPE_NAME, upgradeTypeDTO.getName());
        assertEquals(UP_DESCRIPTION, upgradeTypeDTO.getDescription());
        assertEquals(UP_LIFESPAN, upgradeTypeDTO.getAverageLifespan());
        assertEquals(UP_CARE_LEVEL, upgradeTypeDTO.getCareLevel());
    }

    // ------------------------------------------------------------------
    // Parte C — Filtros y errores
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

        TypeRepository typeRepository = Mockito.mock(TypeRepository.class);
        PetRepository petRepository = Mockito.mock(PetRepository.class);
        TypeMapper typeMapper = Mockito.mock(TypeMapper.class);

        Mockito.when(typeRepository.findAll()).thenReturn(types);
        Mockito.when(petRepository.findAll()).thenReturn(pets);

        TypeService typeService = new TypeServiceImpl(typeRepository, typeMapper, petRepository);

        // Se envía true para excluir tipos inactivos
        List<PetCountByTypeDTO> report = typeService.getPetCountByType(true);

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

        TypeRepository typeRepository = Mockito.mock(TypeRepository.class);
        PetRepository petRepository = Mockito.mock(PetRepository.class);
        TypeMapper typeMapper = Mockito.mock(TypeMapper.class);

        Mockito.when(typeRepository.findById(ID_INEXISTENTE))
                .thenReturn(Optional.empty());

        TypeService typeService = new TypeServiceImpl(typeRepository, typeMapper, petRepository);

        assertThrows(TypeNotFoundException.class, () -> typeService.findById(ID_INEXISTENTE));

        Mockito.verify(typeRepository, Mockito.times(1)).findById(ID_INEXISTENTE);
    }

    @Test
    public void testDeleteType_NotFound() {

        int ID_INEXISTENTE = 999;

        TypeRepository typeRepository = Mockito.mock(TypeRepository.class);
        PetRepository petRepository = Mockito.mock(PetRepository.class);
        TypeMapper typeMapper = Mockito.mock(TypeMapper.class);

        Mockito.when(typeRepository.findById(ID_INEXISTENTE))
                .thenReturn(Optional.empty());

        TypeService typeService = new TypeServiceImpl(typeRepository, typeMapper, petRepository);

        assertThrows(TypeNotFoundException.class, () -> typeService.delete(ID_INEXISTENTE));

        Mockito.verify(typeRepository, Mockito.times(1)).findById(ID_INEXISTENTE);
        Mockito.verify(typeRepository, Mockito.never()).deleteById(ID_INEXISTENTE);
        Mockito.verify(typeRepository, Mockito.never()).delete(Mockito.any(Type.class));
    }
}