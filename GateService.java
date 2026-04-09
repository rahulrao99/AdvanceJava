package com.parking.admin.service;

import com.parking.admin.dto.request.GateRequest;
import com.parking.admin.dto.response.GateResponse;
import com.parking.admin.dto.response.NearestGateResponse;
import com.parking.admin.exception.ResourceAlreadyExistsException;
import com.parking.admin.exception.ResourceNotFoundException;
import com.parking.admin.model.EntryExitGate;
import com.parking.admin.repository.GateRepository;
import com.parking.admin.repository.ParkingLotRepository;
import com.parking.admin.strategy.GateSelectionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GateService {

    private final GateRepository gateRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final GateSelectionStrategy gateSelectionStrategy;

    public GateResponse addGate(String lotId, GateRequest request) {
        log.info("Adding gate {} to lot: {}",
                 request.getGateCode(), lotId);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        if (gateRepository.existsByLotIdAndGateCode(
                lotId, request.getGateCode())) {
            throw new ResourceAlreadyExistsException(
                "Gate " + request.getGateCode()
                + " already exists in this lot");
        }

        EntryExitGate gate = EntryExitGate.builder()
                .id(UUID.randomUUID().toString())
                .lotId(lotId)
                .gateType(request.getGateType())
                .gateCode(request.getGateCode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isActive(true)
                .build();

        gateRepository.save(gate);
        log.info("Gate added: {}", gate.getId());

        return mapToResponse(gate);
    }

    public List<GateResponse> getGatesByLot(String lotId) {
        log.info("Fetching gates for lot: {}", lotId);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        return gateRepository.findByLotId(lotId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public NearestGateResponse getNearestGates(String lotId,
                                                Double userLat,
                                                Double userLon) {
        log.info("Finding nearest gates for lot: {} at {}, {}",
                 lotId, userLat, userLon);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        List<EntryExitGate> gates = gateRepository.findByLotId(lotId);

        if (gates.isEmpty()) {
            throw new ResourceNotFoundException(
                "No gates found for lot: " + lotId);
        }

        return gateSelectionStrategy.findNearestGates(
                gates, userLat, userLon);
    }

    public void deleteGate(String gateId) {
        log.info("Deleting gate: {}", gateId);

        gateRepository.findById(gateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gate not found: " + gateId));

        gateRepository.deleteById(gateId);
        log.info("Gate deleted: {}", gateId);
    }

    private GateResponse mapToResponse(EntryExitGate gate) {
        return GateResponse.builder()
                .id(gate.getId())
                .lotId(gate.getLotId())
                .gateType(gate.getGateType())
                .gateCode(gate.getGateCode())
                .latitude(gate.getLatitude())
                .longitude(gate.getLongitude())
                .isActive(gate.isActive())
                .build();
    }
}
