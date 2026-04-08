package com.parking.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ParkingSpotRequest {

    @NotBlank(message = "Spot code is required")
    private String spotCode;

    @NotBlank(message = "Vehicle type is required")
    @Pattern(
        regexp = "TWO_WHEELER|FOUR_WHEELER|HEAVY_VEHICLE",
        message = "Vehicle type must be TWO_WHEELER, FOUR_WHEELER or HEAVY_VEHICLE"
    )
    private String vehicleType;
}
