package com.parking.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearestGateResponse {
    private GateResponse nearestEntryGate;
    private GateResponse nearestExitGate;
    private Double distanceToEntryGate;
    private Double distanceToExitGate;
}
