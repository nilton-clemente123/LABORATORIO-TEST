package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    List<Owner> findByLastName(String lastName);

    List<Owner> findByCity(String city);

    @Override
    List<Owner> findAll();
}
