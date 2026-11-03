package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.services.VetSpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vet-specialties")
@RequiredArgsConstructor
public class VetSpecialtyController {

    private final VetSpecialtyService vetSpecialtyService;

    @GetMapping
    public List<VetSpecialtyDTO> findAll() {
        return vetSpecialtyService.findAll();
    }

    @GetMapping("/{vetId}/{specialtyId}")
    public VetSpecialtyDTO findById(@PathVariable Integer vetId, @PathVariable Integer specialtyId)
            throws VetSpecialtyNotFoundException {
        return vetSpecialtyService.findById(vetId, specialtyId);
    }

    @GetMapping("/vet/{vetId}")
    public List<VetSpecialtyDTO> findByVetId(@PathVariable Integer vetId) {
        return vetSpecialtyService.findByVetId(vetId);
    }

    @GetMapping("/specialty/{specialtyId}")
    public List<VetSpecialtyDTO> findBySpecialtyId(@PathVariable Integer specialtyId) {
        return vetSpecialtyService.findBySpecialtyId(specialtyId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VetSpecialtyDTO create(@RequestBody VetSpecialtyDTO vetSpecialtyDTO) {
        return vetSpecialtyService.create(vetSpecialtyDTO);
    }

    @PutMapping("/{vetId}/{specialtyId}")
    public VetSpecialtyDTO update(@PathVariable Integer vetId, @PathVariable Integer specialtyId,
                                  @RequestBody VetSpecialtyDTO vetSpecialtyDTO) {
        vetSpecialtyDTO.setVetId(vetId);
        vetSpecialtyDTO.setSpecialtyId(specialtyId);
        return vetSpecialtyService.update(vetSpecialtyDTO);
    }

    @DeleteMapping("/{vetId}/{specialtyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer vetId, @PathVariable Integer specialtyId)
            throws VetSpecialtyNotFoundException {
        vetSpecialtyService.delete(vetId, specialtyId);
    }
}
