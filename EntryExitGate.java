package com.parking.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryExitGate {
    private String id;
    private String lotId;
    private String gateType;
    private String gateCode;
    private Double latitude;
    private Double longitude;
    private boolean isActive;
    private LocalDateTime createdAt;
}
