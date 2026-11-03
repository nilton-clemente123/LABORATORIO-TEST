package com.tecsup.petclinic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Composite primary key for the {@code vet_specialties} join table.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetSpecialtyId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer vetId;

    private Integer specialtyId;
}
