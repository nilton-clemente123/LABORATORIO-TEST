package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    private final VetSpecialtyRepository vetSpecialtyRepository;
    private final VetSpecialtyMapper vetSpecialtyMapper;

    @Override
    public VetSpecialtyDTO create(VetSpecialtyDTO vetSpecialtyDTO) {
        VetSpecialty vetSpecialty = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(vetSpecialtyDTO));
        return vetSpecialtyMapper.mapToDto(vetSpecialty);
    }

    @Override
    public VetSpecialtyDTO update(VetSpecialtyDTO vetSpecialtyDTO) {
        VetSpecialty vetSpecialty = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(vetSpecialtyDTO));
        return vetSpecialtyMapper.mapToDto(vetSpecialty);
    }

    @Override
    public void delete(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException {
        VetSpecialty vetSpecialty = vetSpecialtyRepository
                .findById(new VetSpecialtyId(vetId, specialtyId))
                .orElseThrow(() -> new VetSpecialtyNotFoundException("Record not found...!"));
        vetSpecialtyRepository.delete(vetSpecialty);
    }

    @Override
    public VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException {
        VetSpecialty vetSpecialty = vetSpecialtyRepository
                .findById(new VetSpecialtyId(vetId, specialtyId))
                .orElseThrow(() -> new VetSpecialtyNotFoundException("Record not found...!"));
        return vetSpecialtyMapper.mapToDto(vetSpecialty);
    }

    @Override
    public List<VetSpecialtyDTO> findAll() {
        return vetSpecialtyRepository.findAll().stream()
                .map(vetSpecialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetSpecialtyDTO> findByVetId(Integer vetId) {
        List<VetSpecialty> vetSpecialties = vetSpecialtyRepository.findByVetId(vetId);
        vetSpecialties.forEach(vetSpecialty -> log.info("{}", vetSpecialty));
        return vetSpecialties.stream()
                .map(vetSpecialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetSpecialtyDTO> findBySpecialtyId(Integer specialtyId) {
        List<VetSpecialty> vetSpecialties = vetSpecialtyRepository.findBySpecialtyId(specialtyId);
        vetSpecialties.forEach(vetSpecialty -> log.info("{}", vetSpecialty));
        return vetSpecialties.stream()
                .map(vetSpecialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
