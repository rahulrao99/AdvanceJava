package com.parking.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {
    private String id;
    private String levelId;
    private String spotCode;
    private String vehicleType;
    private boolean isOccupied;
    private boolean isReserved;
    private boolean isActive;
    private LocalDateTime updatedAt;
}
