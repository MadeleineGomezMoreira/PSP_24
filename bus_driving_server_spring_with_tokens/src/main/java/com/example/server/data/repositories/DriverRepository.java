package com.example.server.data.repositories;

import com.example.server.data.model.BusDriverEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<BusDriverEntity, Integer> {

    List<BusDriverEntity> findAll();

    @EntityGraph(attributePaths = {"assignedLine"})
    Optional<BusDriverEntity> findById(int id);

    @Query("SELECT d.assignedLine.id FROM BusDriverEntity d WHERE d.id = ?1")
    Optional<Integer> findAssignedLineIdById(int id);

    @EntityGraph(attributePaths = {"assignedLine"})
    @Query("SELECT d.id FROM BusDriverEntity d JOIN DriverCredentialEntity c ON d.id = c.driver.id WHERE c.username = ?1")
    Optional<Integer> findIdByUsername(String username);

}
