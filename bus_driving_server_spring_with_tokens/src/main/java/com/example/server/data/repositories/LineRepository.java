package com.example.server.data.repositories;

import com.example.server.data.model.BusLineEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends ListCrudRepository<BusLineEntity, Integer> {

    List<BusLineEntity> findAll();

    @EntityGraph(attributePaths = {"busStops"})
    Optional<BusLineEntity> findById(int id);

//    @Query("SELECT bl FROM BusLineEntity bl JOIN bl.busStops bs WHERE bs.id = ?1")
//    List<BusLineEntity> findAllByBusStopId(int id);
}
