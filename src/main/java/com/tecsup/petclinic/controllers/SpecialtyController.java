package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public List<SpecialtyDTO> findAll() {
        return specialtyService.findAll();
    }

    @GetMapping("/{id}")
    public SpecialtyDTO findById(@PathVariable Integer id) throws SpecialtyNotFoundException {
        return specialtyService.findById(id);
    }

    @GetMapping("/name/{name}")
    public List<SpecialtyDTO> findByName(@PathVariable String name) {
        return specialtyService.findByName(name);
    }

    @GetMapping("/office/{office}")
    public List<SpecialtyDTO> findByOffice(@PathVariable String office) {
        return specialtyService.findByOffice(office);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialtyDTO create(@RequestBody SpecialtyDTO specialtyDTO) {
        return specialtyService.create(specialtyDTO);
    }

    @PutMapping("/{id}")
    public SpecialtyDTO update(@PathVariable Integer id, @RequestBody SpecialtyDTO specialtyDTO) {
        specialtyDTO.setId(id);
        return specialtyService.update(specialtyDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws SpecialtyNotFoundException {
        specialtyService.delete(id);
    }
}
