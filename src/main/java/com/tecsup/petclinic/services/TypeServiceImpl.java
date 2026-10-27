package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

	private final TypeRepository typeRepository;
	private final TypeMapper typeMapper;
	private final PetRepository petRepository;

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

	@Override
	public List<PetCountByTypeDTO> getPetCountByType(boolean excludeInactive) {

		List<Type> types = typeRepository.findAll();

		if (excludeInactive) {
			types = types.stream()
					.filter(type -> Boolean.TRUE.equals(type.getActive()))
					.collect(Collectors.toList());
		}

		Map<Integer, Long> petCountByTypeId = petRepository.findAll().stream()
				.collect(Collectors.groupingBy(Pet::getTypeId, Collectors.counting()));

		return types.stream()
				.map(type -> PetCountByTypeDTO.builder()
						.typeId(type.getId())
						.typeName(type.getName())
						.petCount(petCountByTypeId.getOrDefault(type.getId(), 0L))
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) throws TypeNotFoundException {

		Type type = typeRepository.findById(id)
				.orElseThrow(() -> new TypeNotFoundException("Record not found...!"));

		typeRepository.delete(type);
	}
}
