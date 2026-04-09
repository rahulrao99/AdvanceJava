package com.parking.admin.controller;

import com.parking.admin.dto.request.ParkingLevelRequest;
import com.parking.admin.dto.response.ApiResponse;
import com.parking.admin.dto.response.ParkingLevelResponse;
import com.parking.admin.service.ParkingLevelService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ParkingLevelController {

    private final ParkingLevelService parkingLevelService;

    @PostMapping("/lots/{lotId}/levels")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<ParkingLevelResponse>> addLevel(
            @PathVariable String lotId,
            @Valid @RequestBody ParkingLevelRequest request) {
        log.info("Add level request for lot: {}", lotId);
        ParkingLevelResponse response =
                parkingLevelService.addLevel(lotId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Level added successfully", response));
    }

    @GetMapping("/lots/{lotId}/levels")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ParkingLevelResponse>>>
            getLevelsByLot(@PathVariable String lotId) {
        log.info("Get levels for lot: {}", lotId);
        List<ParkingLevelResponse> response =
                parkingLevelService.getLevelsByLot(lotId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Levels fetched successfully", response));
    }

    @DeleteMapping("/levels/{levelId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLevel(
            @PathVariable String levelId) {
        log.info("Delete level request: {}", levelId);
        parkingLevelService.deleteLevel(levelId);
        return ResponseEntity.ok(
                ApiResponse.success("Level deleted successfully"));
    }
}
