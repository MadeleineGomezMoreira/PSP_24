package com.example.server.data.repositories;

import com.example.server.data.model.BusStopEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface StopRepository extends JpaRepository<BusStopEntity, Integer> {

    @EntityGraph(attributePaths = {"busLines"})
    Optional<BusStopEntity> findById(int id);

}
