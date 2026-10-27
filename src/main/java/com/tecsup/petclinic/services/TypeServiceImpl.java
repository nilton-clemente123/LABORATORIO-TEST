package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

	private final TypeRepository typeRepository;
	private final TypeMapper typeMapper;

	@Override
	public TypeDTO create(TypeDTO typeDTO) {

		Type newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));

		return typeMapper.mapToDto(newType);
	}

	@Override
	public TypeDTO update(TypeDTO typeDTO) {

		Type updatedType = typeRepository.save(typeMapper.mapToEntity(typeDTO));

		return typeMapper.mapToDto(updatedType);
	}

	@Override
	public TypeDTO findById(Integer id) throws TypeNotFoundException {

		Type type = typeRepository.findById(id)
				.orElseThrow(() -> new TypeNotFoundException("Record not found...!"));

		return typeMapper.mapToDto(type);
	}
}
