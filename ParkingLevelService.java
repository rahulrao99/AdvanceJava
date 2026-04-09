package com.parking.admin.service;

import com.parking.admin.dto.request.ParkingLevelRequest;
import com.parking.admin.dto.response.ParkingLevelResponse;
import com.parking.admin.exception.ResourceAlreadyExistsException;
import com.parking.admin.exception.ResourceNotFoundException;
import com.parking.admin.model.ParkingLevel;
import com.parking.admin.repository.ParkingLevelRepository;
import com.parking.admin.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingLevelService {

    private final ParkingLevelRepository parkingLevelRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ParkingLevelResponse addLevel(String lotId,
                                          ParkingLevelRequest request) {
        log.info("Adding level {} to lot: {}", 
                 request.getLevelNumber(), lotId);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        if (parkingLevelRepository.existsByLotIdAndLevelNumber(
                lotId, request.getLevelNumber())) {
            throw new ResourceAlreadyExistsException(
                "Level " + request.getLevelNumber() 
                + " already exists in this lot");
        }

        ParkingLevel level = ParkingLevel.builder()
                .id(UUID.randomUUID().toString())
                .lotId(lotId)
                .levelNumber(request.getLevelNumber())
                .label(request.getLabel())
                .totalSpots(request.getTotalSpots())
                .build();

        parkingLevelRepository.save(level);
        log.info("Level added: {}", level.getId());

        return mapToResponse(level);
    }

    public List<ParkingLevelResponse> getLevelsByLot(String lotId) {
        log.info("Fetching levels for lot: {}", lotId);

        parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking lot not found: " + lotId));

        return parkingLevelRepository.findByLotId(lotId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteLevel(String levelId) {
        log.info("Deleting level: {}", levelId);

        parkingLevelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Level not found: " + levelId));

        parkingLevelRepository.deleteById(levelId);
        log.info("Level deleted: {}", levelId);
    }

    private ParkingLevelResponse mapToResponse(ParkingLevel level) {
        return ParkingLevelResponse.builder()
                .id(level.getId())
                .lotId(level.getLotId())
                .levelNumber(level.getLevelNumber())
                .label(level.getLabel())
                .totalSpots(level.getTotalSpots())
                .build();
    }
}
