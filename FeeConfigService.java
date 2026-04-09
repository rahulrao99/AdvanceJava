package com.parking.admin.service;

import com.parking.admin.dto.request.FeeConfigRequest;
import com.parking.admin.dto.response.FeeConfigResponse;
import com.parking.admin.exception.ResourceAlreadyExistsException;
import com.parking.admin.exception.ResourceNotFoundException;
import com.parking.admin.model.FeeConfig;
import com.parking.admin.repository.FeeConfigRepository;
import com.parking.admin.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeeConfigService {

    private final FeeConfigRepository feeConfigRepository;
    private final ParkingLotRepository parkingLotRepository;

    public FeeConfigResponse createFeeConfig(String lotId,
                                              FeeConfigRequest request) {
        log.info("Creating fee config for lot: {}", lotId);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        if (feeConfigRepository.existsByLotIdAndVehicleType(
                lotId, request.getVehicleType())) {
            throw new ResourceAlreadyExistsException(
                "Fee config for " + request.getVehicleType()
                + " already exists for this lot");
        }

        FeeConfig feeConfig = FeeConfig.builder()
                .id(UUID.randomUUID().toString())
                .lotId(lotId)
                .vehicleType(request.getVehicleType())
                .ratePerHour(request.getRatePerHour())
                .graceMinutes(request.getGraceMinutes())
                .isActive(true)
                .build();

        feeConfigRepository.save(feeConfig);
        log.info("Fee config created: {}", feeConfig.getId());

        return mapToResponse(feeConfig);
    }

    public List<FeeConfigResponse> getFeeConfigByLot(String lotId) {
        log.info("Fetching fee config for lot: {}", lotId);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        return feeConfigRepository.findByLotId(lotId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FeeConfigResponse updateFeeConfig(String feeId,
                                              FeeConfigRequest request) {
        log.info("Updating fee config: {}", feeId);

        FeeConfig feeConfig = feeConfigRepository.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fee config not found: " + feeId));

        feeConfig.setRatePerHour(request.getRatePerHour());
        feeConfig.setGraceMinutes(request.getGraceMinutes());

        feeConfigRepository.update(feeConfig);
        log.info("Fee config updated: {}", feeId);

        return feeConfigRepository.findById(feeId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fee config not found after update"));
    }

    private FeeConfigResponse mapToResponse(FeeConfig feeConfig) {
        return FeeConfigResponse.builder()
                .id(feeConfig.getId())
                .lotId(feeConfig.getLotId())
                .vehicleType(feeConfig.getVehicleType())
                .ratePerHour(feeConfig.getRatePerHour())
                .graceMinutes(feeConfig.getGraceMinutes())
                .isActive(feeConfig.isActive())
                .build();
    }
}
