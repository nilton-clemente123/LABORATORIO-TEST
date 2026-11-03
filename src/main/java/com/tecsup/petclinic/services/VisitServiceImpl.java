package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mappers.VisitMapper;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Override
    public VisitDTO create(VisitDTO visitDTO) {
        Visit visit = visitRepository.save(visitMapper.mapToEntity(visitDTO));
        return visitMapper.mapToDto(visit);
    }

    @Override
    public VisitDTO update(VisitDTO visitDTO) {
        Visit visit = visitRepository.save(visitMapper.mapToEntity(visitDTO));
        return visitMapper.mapToDto(visit);
    }

    @Override
    public void delete(Integer id) throws VisitNotFoundException {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Record not found...!"));
        visitRepository.delete(visit);
    }

    @Override
    public VisitDTO findById(Integer id) throws VisitNotFoundException {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Record not found...!"));
        return visitMapper.mapToDto(visit);
    }

    @Override
    public List<VisitDTO> findAll() {
        return visitRepository.findAll().stream()
                .map(visitMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitDTO> findByPetId(Integer petId) {
        List<Visit> visits = visitRepository.findByPetId(petId);
        visits.forEach(visit -> log.info("{}", visit));
        return visits.stream()
                .map(visitMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitDTO> findByVetId(Integer vetId) {
        List<Visit> visits = visitRepository.findByVetId(vetId);
        visits.forEach(visit -> log.info("{}", visit));
        return visits.stream()
                .map(visitMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
