package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Relation between a veterinarian and a specialty. Mapped to table
 * {@code vet_specialties} (many-to-many join).
 */
@Entity(name = "vet_specialties")
@IdClass(VetSpecialtyId.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VetSpecialty {

    @Id
    @Column(name = "vet_id")
    private Integer vetId;

    @Id
    @Column(name = "specialty_id")
    private Integer specialtyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "certification_date")
    private LocalDate certificationDate;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    private String notes;
}
