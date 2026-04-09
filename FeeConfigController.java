package com.parking.admin.controller;

import com.parking.admin.dto.request.FeeConfigRequest;
import com.parking.admin.dto.response.ApiResponse;
import com.parking.admin.dto.response.FeeConfigResponse;
import com.parking.admin.service.FeeConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class FeeConfigController {

    private final FeeConfigService feeConfigService;

    @PostMapping("/lots/{lotId}/fees")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FeeConfigResponse>> createFeeConfig(
            @PathVariable String lotId,
            @Valid @RequestBody FeeConfigRequest request) {
        log.info("Create fee config for lot: {}", lotId);
        FeeConfigResponse response =
                feeConfigService.createFeeConfig(lotId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Fee config created successfully", response));
    }

    @GetMapping("/lots/{lotId}/fees")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER')")
    public ResponseEntity<ApiResponse<List<FeeConfigResponse>>>
            getFeeConfig(@PathVariable String lotId) {
        log.info("Get fee config for lot: {}", lotId);
        List<FeeConfigResponse> response =
                feeConfigService.getFeeConfigByLot(lotId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Fee config fetched successfully", response));
    }

    @PutMapping("/fees/{feeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FeeConfigResponse>> updateFeeConfig(
            @PathVariable String feeId,
            @Valid @RequestBody FeeConfigRequest request) {
        log.info("Update fee config: {}", feeId);
        FeeConfigResponse response =
                feeConfigService.updateFeeConfig(feeId, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Fee config updated successfully", response));
    }
}
