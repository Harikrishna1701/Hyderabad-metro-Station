package com.example.metro.controller;

import com.example.metro.dto.RouteRequest;
import com.example.metro.dto.RouteResponse;
import com.example.metro.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/find")
    public ResponseEntity<RouteResponse> findRoute(@RequestBody RouteRequest request) {
        return ResponseEntity.ok(routeService.findRoute(request));
    }
}
