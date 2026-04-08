package com.parking.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GateResponse {
    private String id;
    private String lotId;
    private String gateType;
    private String gateCode;
    private Double latitude;
    private Double longitude;
    private boolean isActive;
}
