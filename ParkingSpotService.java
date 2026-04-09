package com.parking.admin.service;

import com.parking.admin.dto.request.ParkingSpotRequest;
import com.parking.admin.dto.response.ParkingSpotResponse;
import com.parking.admin.exception.ResourceAlreadyExistsException;
import com.parking.admin.exception.ResourceNotFoundException;
import com.parking.admin.model.ParkingSpot;
import com.parking.admin.repository.ParkingLevelRepository;
import com.parking.admin.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingLevelRepository parkingLevelRepository;

    public ParkingSpotResponse addSpot(String levelId,
                                        ParkingSpotRequest request) {
        log.info("Adding spot {} to level: {}",
                 request.getSpotCode(), levelId);

        parkingLevelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Level not found: " + levelId));

        if (parkingSpotRepository.existsByLevelIdAndSpotCode(
                levelId, request.getSpotCode())) {
            throw new ResourceAlreadyExistsException(
                "Spot " + request.getSpotCode()
                + " already exists in this level");
        }

        ParkingSpot spot = ParkingSpot.builder()
                .id(UUID.randomUUID().toString())
                .levelId(levelId)
                .spotCode(request.getSpotCode())
                .vehicleType(request.getVehicleType())
                .isOccupied(false)
                .isReserved(false)
                .isActive(true)
                .build();

        parkingSpotRepository.save(spot);
        log.info("Spot added: {}", spot.getId());

        return mapToResponse(spot);
    }

    public List<ParkingSpotResponse> getSpotsByLevel(String levelId) {
        log.info("Fetching spots for level: {}", levelId);

        parkingLevelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Level not found: " + levelId));

        return parkingSpotRepository.findByLevelId(levelId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ParkingSpotResponse updateSpot(String spotId,
                                           ParkingSpotRequest request) {
        log.info("Updating spot: {}", spotId);

        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Spot not found: " + spotId));

        spot.setSpotCode(request.getSpotCode());
        spot.setVehicleType(request.getVehicleType());

        parkingSpotRepository.update(spot);
        log.info("Spot updated: {}", spotId);

        return parkingSpotRepository.findById(spotId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Spot not found after update"));
    }

    public void deleteSpot(String spotId) {
        log.info("Deleting spot: {}", spotId);

        parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Spot not found: " + spotId));

        parkingSpotRepository.deleteById(spotId);
        log.info("Spot deleted: {}", spotId);
    }

    private ParkingSpotResponse mapToResponse(ParkingSpot spot) {
        return ParkingSpotResponse.builder()
                .id(spot.getId())
                .levelId(spot.getLevelId())
                .spotCode(spot.getSpotCode())
                .vehicleType(spot.getVehicleType())
                .isOccupied(spot.isOccupied())
                .isReserved(spot.isReserved())
                .isActive(spot.isActive())
                .build();
    }
}
