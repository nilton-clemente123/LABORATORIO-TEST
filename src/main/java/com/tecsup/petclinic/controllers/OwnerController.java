package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping
    public List<OwnerDTO> findAll() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public OwnerDTO findById(@PathVariable Integer id) throws OwnerNotFoundException {
        return ownerService.findById(id);
    }

    @GetMapping("/lastname/{lastName}")
    public List<OwnerDTO> findByLastName(@PathVariable String lastName) {
        return ownerService.findByLastName(lastName);
    }

    @GetMapping("/city/{city}")
    public List<OwnerDTO> findByCity(@PathVariable String city) {
        return ownerService.findByCity(city);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerDTO create(@RequestBody OwnerDTO ownerDTO) {
        return ownerService.create(ownerDTO);
    }

    @PutMapping("/{id}")
    public OwnerDTO update(@PathVariable Integer id, @RequestBody OwnerDTO ownerDTO) {
        ownerDTO.setId(id);
        return ownerService.update(ownerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws OwnerNotFoundException {
        ownerService.delete(id);
    }
}
