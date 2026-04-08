package com.parking.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpotResponse {
    private String id;
    private String levelId;
    private String spotCode;
    private String vehicleType;
    private boolean isOccupied;
    private boolean isReserved;
    private boolean isActive;
}
