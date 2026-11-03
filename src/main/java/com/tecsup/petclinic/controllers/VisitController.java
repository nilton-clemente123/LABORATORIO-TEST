package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public List<VisitDTO> findAll() {
        return visitService.findAll();
    }

    @GetMapping("/{id}")
    public VisitDTO findById(@PathVariable Integer id) throws VisitNotFoundException {
        return visitService.findById(id);
    }

    @GetMapping("/pet/{petId}")
    public List<VisitDTO> findByPetId(@PathVariable Integer petId) {
        return visitService.findByPetId(petId);
    }

    @GetMapping("/vet/{vetId}")
    public List<VisitDTO> findByVetId(@PathVariable Integer vetId) {
        return visitService.findByVetId(vetId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDTO create(@RequestBody VisitDTO visitDTO) {
        return visitService.create(visitDTO);
    }

    @PutMapping("/{id}")
    public VisitDTO update(@PathVariable Integer id, @RequestBody VisitDTO visitDTO) {
        visitDTO.setId(id);
        return visitService.update(visitDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws VisitNotFoundException {
        visitService.delete(id);
    }
}
