// model/Station.java
package com.example.metro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    @JsonBackReference
    private Line line;

    @Column(name = "station_number")
    private Integer stationNumber;

    @Column(name = "distance_from_previous")
    private java.math.BigDecimal distanceFromPrevious;

    @Column(name = "is_interchange")
    private Boolean isInterchange = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
// Note: The `distanceFromPrevious` field is of type `java.math.BigDecimal` to handle precise distance values.