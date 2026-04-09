@GetMapping("/spots/{spotId}")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER','ROLE_USER')")
public ResponseEntity<ApiResponse<ParkingSpotResponse>> getSpotById(
        @PathVariable("spotId") String spotId) {
    log.info("Get spot by id: {}", spotId);
    ParkingSpot spot = parkingSpotRepository.findById(spotId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Spot not found: " + spotId));
    ParkingSpotResponse response = ParkingSpotResponse.builder()
            .id(spot.getId())
            .levelId(spot.getLevelId())
            .spotCode(spot.getSpotCode())
            .vehicleType(spot.getVehicleType())
            .isOccupied(spot.isOccupied())
            .isReserved(spot.isReserved())
            .isActive(spot.isActive())
            .build();
    return ResponseEntity.ok(
            ApiResponse.success("Spot fetched", response));
}
