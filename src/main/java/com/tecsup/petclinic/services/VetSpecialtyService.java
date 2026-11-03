package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

    VetSpecialtyDTO create(VetSpecialtyDTO vetSpecialtyDTO);

    VetSpecialtyDTO update(VetSpecialtyDTO vetSpecialtyDTO);

    void delete(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

    VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

    List<VetSpecialtyDTO> findAll();

    List<VetSpecialtyDTO> findByVetId(Integer vetId);

    List<VetSpecialtyDTO> findBySpecialtyId(Integer specialtyId);
}
