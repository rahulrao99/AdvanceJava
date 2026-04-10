package com.parking.booking.feign;

import com.parking.booking.dto.response.ApiResponse;
import com.parking.booking.dto.response.FeeConfigResponse;
import com.parking.booking.dto.response.SpotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "admin-service")
public interface AdminServiceClient {

    @GetMapping("/api/admin/spots/{spotId}")
    ApiResponse<SpotResponse> getSpotById(
            @RequestHeader("Authorization") String token,
            @PathVariable("spotId") String spotId);

    @PutMapping("/api/admin/spots/{spotId}/occupy")
    ApiResponse<Void> markSpotOccupied(
            @RequestHeader("Authorization") String token,
            @PathVariable("spotId") String spotId,
            @RequestParam("occupied") boolean occupied);

    @GetMapping("/api/admin/lots/{lotId}/fees")
    ApiResponse<List<FeeConfigResponse>> getFeeConfigByLot(
            @RequestHeader("Authorization") String token,
            @PathVariable("lotId") String lotId);

    @GetMapping("/api/admin/spots/{spotId}/lot")
    ApiResponse<String> getLotIdBySpotId(
            @RequestHeader("Authorization") String token,
            @PathVariable("spotId") String spotId);
}
