package com.example.metro.service;

import com.example.metro.dto.RouteRequest;
import com.example.metro.dto.RouteResponse;
import com.example.metro.graph.MetroGraph;
import com.example.metro.model.Station;
import com.example.metro.repository.StationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final StationRepository stationRepository;

    @Transactional
    public RouteResponse findRoute(RouteRequest request) {
        String source = request.getSource();
        String destination = request.getDestination();

        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = allStations.stream()
                .filter(s -> s.getName().equalsIgnoreCase(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Source station not found"));

        Station destinationStation = allStations.stream()
                .filter(s -> s.getName().equalsIgnoreCase(destination))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Destination station not found"));

        Map<String, Station> stationKeyMap = new HashMap<>();
        MetroGraph graph = new MetroGraph();

        Map<Long, List<Station>> stationsByLine = allStations.stream()
                .collect(Collectors.groupingBy(s -> s.getLine().getId()));

        for (List<Station> lineStations : stationsByLine.values()) {
            lineStations.sort(Comparator.comparingInt(Station::getStationNumber));
            for (int i = 1; i < lineStations.size(); i++) {
                Station prev = lineStations.get(i - 1);
                Station curr = lineStations.get(i);

                String prevKey = prev.getName() + "_" + prev.getLine().getId();
                String currKey = curr.getName() + "_" + curr.getLine().getId();

                graph.addEdge(prevKey, currKey, curr.getDistanceFromPrevious().doubleValue());

                graph.registerStationName(prevKey, prev.getName());
                graph.registerStationName(currKey, curr.getName());

                stationKeyMap.put(prevKey, prev);
                stationKeyMap.put(currKey, curr);
            }
        }

        Map<String, List<Station>> interchangeGroups = allStations.stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsInterchange()))
                .collect(Collectors.groupingBy(Station::getName));

        for (List<Station> interchangeList : interchangeGroups.values()) {
            for (int i = 0; i < interchangeList.size(); i++) {
                for (int j = i + 1; j < interchangeList.size(); j++) {
                    String key1 = interchangeList.get(i).getName() + "_" + interchangeList.get(i).getLine().getId();
                    String key2 = interchangeList.get(j).getName() + "_" + interchangeList.get(j).getLine().getId();
                    graph.addEdge(key1, key2, 0.0);
                }
            }
        }

        String sourceKey = sourceStation.getName() + "_" + sourceStation.getLine().getId();
        String destKey = destinationStation.getName() + "_" + destinationStation.getLine().getId();

        List<String> rawRoute = graph.findShortestPath(sourceKey, destKey);

        if (rawRoute.isEmpty()) {
            throw new RuntimeException("No route found between " + source + " and " + destination);
        }

        List<String> route = rawRoute.stream().map(graph.getKeyToName()::get).collect(Collectors.toList());
        int totalStations = route.size();
        double totalDistance = 0;
        List<String> interchanges = new ArrayList<>();
        List<RouteResponse.LineChange> lineChanges = new ArrayList<>();

        String prevLine = null;
        String prevStationName = null;

        for (int i = 0; i < rawRoute.size(); i++) {
            String stationKey = rawRoute.get(i);
            Station s = stationKeyMap.get(stationKey);

            if (i > 0) {
                totalDistance += s.getDistanceFromPrevious().doubleValue();
            }

            String currentLine = s.getLine().getName();
            if (prevLine != null && !prevLine.equals(currentLine)) {
                interchanges.add(prevStationName);
                RouteResponse.LineChange change = new RouteResponse.LineChange();
                change.setFrom(prevLine);
                change.setTo(currentLine);
                change.setAt(prevStationName);
                lineChanges.add(change);
            }

            prevLine = currentLine;
            prevStationName = s.getName();
        }

        double baseFare = 10.0;
        double additionalFare = Math.max(0, totalStations - 2) * 5.0;
        double interchangePenalty = interchanges.size() * 2.0;
        double totalFare = baseFare + additionalFare + interchangePenalty;

        int estimatedTime = (int) ((totalStations - 1) * 2.5 + interchanges.size() * 5);

        return RouteResponse.builder()
                .route(route)
                .totalStations(totalStations)
                .totalDistance(BigDecimal.valueOf(totalDistance))
                .totalFare(BigDecimal.valueOf(totalFare))
                .interchanges(interchanges)
                .estimatedTime(estimatedTime + " minutes")
                .lineChanges(lineChanges)
                .build();
    }
}
