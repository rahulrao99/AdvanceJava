package com.parking.admin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkingLevelRequest {

    @NotNull(message = "Level number is required")
    @Min(value = 0, message = "Level number must be 0 or greater")
    private Integer levelNumber;

    @NotBlank(message = "Label is required")
    private String label;

    @NotNull(message = "Total spots is required")
    @Min(value = 1, message = "Minimum 1 spot required")
    private Integer totalSpots;
}
