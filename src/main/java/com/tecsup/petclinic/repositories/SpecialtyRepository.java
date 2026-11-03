package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {

    List<Specialty> findByName(String name);

    List<Specialty> findByOffice(String office);

    @Override
    List<Specialty> findAll();
}
