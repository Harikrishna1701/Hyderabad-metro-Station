package com.example.metro.service;

import com.example.metro.model.Line;
import com.example.metro.model.Station;
import com.example.metro.repository.LineRepository;
import com.example.metro.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public Station addStationToLine(Long lineId, Station station) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("Line not found with id: " + lineId));
        station.setLine(line);
        return stationRepository.save(station);
    }

    public List<Station> getStationsByLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("Line not found with id: " + lineId));
        return stationRepository.findByLine(line);
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }
}