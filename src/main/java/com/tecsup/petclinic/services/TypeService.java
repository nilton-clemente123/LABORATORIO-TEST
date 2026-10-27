package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

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
}