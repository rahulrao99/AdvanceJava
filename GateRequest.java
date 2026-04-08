package com.parking.admin.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GateRequest {

    @NotBlank(message = "Gate code is required")
    private String gateCode;

    @NotBlank(message = "Gate type is required")
    @Pattern(
        regexp = "ENTRY|EXIT|BOTH",
        message = "Gate type must be ENTRY, EXIT or BOTH"
    )
    private String gateType;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Invalid latitude")
    @DecimalMax(value = "90.0", message = "Invalid latitude")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Invalid longitude")
    @DecimalMax(value = "180.0", message = "Invalid longitude")
    private Double longitude;
}
