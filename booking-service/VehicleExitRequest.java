package com.parking.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleExitRequest {

    @NotBlank(message = "Exit gate ID is required")
    private String exitGateId;
}
