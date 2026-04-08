package com.parking.admin.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeConfigRequest {

    @NotBlank(message = "Vehicle type is required")
    @Pattern(
        regexp = "TWO_WHEELER|FOUR_WHEELER|HEAVY_VEHICLE",
        message = "Vehicle type must be TWO_WHEELER, FOUR_WHEELER or HEAVY_VEHICLE"
    )
    private String vehicleType;

    @NotNull(message = "Rate per hour is required")
    @DecimalMin(value = "0.1", message = "Rate must be greater than 0")
    private BigDecimal ratePerHour;

    @Min(value = 0, message = "Grace minutes cannot be negative")
    private Integer graceMinutes = 15;
}
