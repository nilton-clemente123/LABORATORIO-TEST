package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @GetMapping
    public List<VetDTO> findAll() {
        return vetService.findAll();
    }

    @GetMapping("/{id}")
    public VetDTO findById(@PathVariable Integer id) throws VetNotFoundException {
        return vetService.findById(id);
    }

    @GetMapping("/lastname/{lastName}")
    public List<VetDTO> findByLastName(@PathVariable String lastName) {
        return vetService.findByLastName(lastName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VetDTO create(@RequestBody VetDTO vetDTO) {
        return vetService.create(vetDTO);
    }

    @PutMapping("/{id}")
    public VetDTO update(@PathVariable Integer id, @RequestBody VetDTO vetDTO) {
        vetDTO.setId(id);
        return vetService.update(vetDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws VetNotFoundException {
        vetService.delete(id);
    }
}
