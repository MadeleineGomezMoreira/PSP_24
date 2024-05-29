package com.example.server.data.repositories;

import com.example.server.data.model.BusLineEntity;
import com.example.server.data.model.DriverCredentialEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository extends ListCrudRepository<DriverCredentialEntity, Integer> {

    List<DriverCredentialEntity> findAll();

    Optional<DriverCredentialEntity> findById(int id);

    Optional<DriverCredentialEntity> findByEmail(String email);

    Optional<DriverCredentialEntity> findByUsername(String username);

}
