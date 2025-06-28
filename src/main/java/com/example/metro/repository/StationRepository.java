package com.example.metro.repository;

import com.example.metro.model.Station;
import com.example.metro.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByName(String name);         // already needed for RouteService
    List<Station> findByLine(Line line);                // ✅ Add this method
}
