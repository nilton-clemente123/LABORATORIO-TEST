package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

import java.util.List;

public interface TypeService {

	/**
	 * Creates a new pet type.
	 */
	TypeDTO create(TypeDTO typeDTO);

	/**
	 * Updates an existing pet type.
	 */
	TypeDTO update(TypeDTO typeDTO);

	/**
	 * Finds a pet type by id.
	 *
	 * @throws TypeNotFoundException if no type exists with that id
	 */
	TypeDTO findById(Integer id) throws TypeNotFoundException;

	/**
	 * Returns a report with the number of pets grouped by type.
	 *
	 * @param excludeInactive when true, inactive types are excluded from the report
	 */
	List<PetCountByTypeDTO> getPetCountByType(boolean excludeInactive);

	/**
	 * Deletes a pet type by id.
	 *
	 * @throws TypeNotFoundException if no type exists with that id
	 */
	void delete(Integer id) throws TypeNotFoundException;
}