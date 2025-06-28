package com.example.metro.controller;

import com.example.metro.model.Line;
import com.example.metro.service.LineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<Line> createLine(@RequestBody Line line) {
        return ResponseEntity.ok(lineService.createLine(line));
    }

    @GetMapping
    public ResponseEntity<List<Line>> getAllLines() {
        return ResponseEntity.ok(lineService.getAllLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<Line> getLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.getLineById(lineId));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Line> updateLine(@PathVariable Long lineId, @RequestBody Line line) {
        return ResponseEntity.ok(lineService.updateLine(lineId, line));
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}