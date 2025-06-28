package com.example.metro.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private List<String> route;
    private int totalStations;
    private BigDecimal totalDistance;
    private BigDecimal totalFare;
    private List<String> interchanges;
    private String estimatedTime;
    private List<LineChange> lineChanges;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LineChange {
        private String from;
        private String to;
        private String at;
    }
}
