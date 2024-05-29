package com.example.server.data.repositories;

import com.example.server.data.model.DriverCredentialEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface AuthRepository extends ListCrudRepository<DriverCredentialEntity, Integer> {

    @EntityGraph(attributePaths = {"role"})
    Optional<DriverCredentialEntity> findDriverCredentialEntityByUsername(String username);
    @EntityGraph(attributePaths = {"role"})
    Optional<DriverCredentialEntity> findDriverCredentialEntityByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<DriverCredentialEntity> findDriverCredentialEntityById(int id);

}
