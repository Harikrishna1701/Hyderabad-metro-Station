package com.example.metro.service;

import com.example.metro.model.Line;
import com.example.metro.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public Line createLine(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> getAllLines() {
        return lineRepository.findAll();
    }

    public Line getLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("Line not found with id: " + lineId));
    }

   public Line updateLine(Long lineId, Line newLine) {
    Line existing = getLineById(lineId);
    existing.setName(newLine.getName());  // 🔁 fixed here
    existing.setColor(newLine.getColor());
    return lineRepository.save(existing);
}


    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}