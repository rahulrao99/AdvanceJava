package com.parking.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLevelResponse {
    private String id;
    private String lotId;
    private Integer levelNumber;
    private String label;
    private Integer totalSpots;
}
