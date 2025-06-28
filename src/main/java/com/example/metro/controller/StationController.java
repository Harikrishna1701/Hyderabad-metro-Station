package com.example.metro.controller;

import com.example.metro.model.Station;
import com.example.metro.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Station> addStationToLine(@PathVariable Long lineId, @RequestBody Station station) {
        return ResponseEntity.ok(stationService.addStationToLine(lineId, station));
    }

    @GetMapping("/lines/{lineId}/stations")
    public ResponseEntity<List<Station>> getStationsByLine(@PathVariable Long lineId) {
        return ResponseEntity.ok(stationService.getStationsByLine(lineId));
    }

    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }
}



