package com.parking.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeConfigResponse {
    private String id;
    private String lotId;
    private String vehicleType;
    private BigDecimal ratePerHour;
    private Integer graceMinutes;
    private boolean isActive;
}
