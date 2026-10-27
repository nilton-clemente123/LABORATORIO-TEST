package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}