package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
public class TypesController {

    private final TypeService typeService;

    @GetMapping
    public List<TypeDTO> findAll() {
        return typeService.findAll();
    }

    @GetMapping("/{id}")
    public TypeDTO findById(@PathVariable Integer id) throws TypeNotFoundException {
        return typeService.findById(id);
    }

    @GetMapping("/report")
    public List<PetCountByTypeDTO> getPetCountByType(
            @RequestParam(name = "excludeInactive", defaultValue = "false") boolean excludeInactive) {
        return typeService.getPetCountByType(excludeInactive);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeDTO create(@RequestBody TypeDTO typeDTO) {
        return typeService.create(typeDTO);
    }

    @PutMapping("/{id}")
    public TypeDTO update(@PathVariable Integer id, @RequestBody TypeDTO typeDTO) {
        typeDTO.setId(id);
        return typeService.update(typeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws TypeNotFoundException {
        typeService.delete(id);
    }
}
