package com.parking.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeConfig {
    private String id;
    private String lotId;
    private String vehicleType;
    private BigDecimal ratePerHour;
    private Integer graceMinutes;
    private boolean isActive;
    private LocalDateTime createdAt;
}
