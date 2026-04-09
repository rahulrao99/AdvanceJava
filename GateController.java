package com.parking.admin.controller;

import com.parking.admin.dto.request.GateRequest;
import com.parking.admin.dto.response.ApiResponse;
import com.parking.admin.dto.response.GateResponse;
import com.parking.admin.dto.response.NearestGateResponse;
import com.parking.admin.service.GateService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class GateController {

    private final GateService gateService;

    @PostMapping("/lots/{lotId}/gates")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<GateResponse>> addGate(
            @PathVariable String lotId,
            @Valid @RequestBody GateRequest request) {
        log.info("Add gate request for lot: {}", lotId);
        GateResponse response = gateService.addGate(lotId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Gate added successfully", response));
    }

    @GetMapping("/lots/{lotId}/gates")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<List<GateResponse>>> getGates(
            @PathVariable String lotId) {
        log.info("Get gates for lot: {}", lotId);
        List<GateResponse> response =
                gateService.getGatesByLot(lotId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Gates fetched successfully", response));
    }

    @GetMapping("/lots/{lotId}/nearest-gate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER','ROLE_USER')")
    public ResponseEntity<ApiResponse<NearestGateResponse>>
            getNearestGate(
                @PathVariable String lotId,
                @RequestParam Double latitude,
                @RequestParam Double longitude) {
        log.info("Nearest gate request for lot: {} at {}, {}",
                 lotId, latitude, longitude);
        NearestGateResponse response =
                gateService.getNearestGates(lotId, latitude, longitude);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Nearest gates found", response));
    }

    @DeleteMapping("/gates/{gateId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGate(
            @PathVariable String gateId) {
        log.info("Delete gate request: {}", gateId);
        gateService.deleteGate(gateId);
        return ResponseEntity.ok(
                ApiResponse.success("Gate deleted successfully"));
    }
}
