package com.parking.admin.controller;

import com.parking.admin.dto.request.ParkingSpotRequest;
import com.parking.admin.dto.response.ApiResponse;
import com.parking.admin.dto.response.ParkingSpotResponse;
import com.parking.admin.service.ParkingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @PostMapping("/levels/{levelId}/spots")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<ParkingSpotResponse>> addSpot(
            @PathVariable String levelId,
            @Valid @RequestBody ParkingSpotRequest request) {
        log.info("Add spot request for level: {}", levelId);
        ParkingSpotResponse response =
                parkingSpotService.addSpot(levelId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Spot added successfully", response));
    }

    @GetMapping("/levels/{levelId}/spots")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ParkingSpotResponse>>>
            getSpotsByLevel(@PathVariable String levelId) {
        log.info("Get spots for level: {}", levelId);
        List<ParkingSpotResponse> response =
                parkingSpotService.getSpotsByLevel(levelId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Spots fetched successfully", response));
    }

    @PutMapping("/spots/{spotId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<ParkingSpotResponse>> updateSpot(
            @PathVariable String spotId,
            @Valid @RequestBody ParkingSpotRequest request) {
        log.info("Update spot request: {}", spotId);
        ParkingSpotResponse response =
                parkingSpotService.updateSpot(spotId, request);
        return ResponseEntity.ok(
                ApiResponse.success("Spot updated successfully", response));
    }

    @DeleteMapping("/spots/{spotId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSpot(
            @PathVariable String spotId) {
        log.info("Delete spot request: {}", spotId);
        parkingSpotService.deleteSpot(spotId);
        return ResponseEntity.ok(
                ApiResponse.success("Spot deleted successfully"));
    }
}
