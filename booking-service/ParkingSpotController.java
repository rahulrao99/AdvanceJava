@PutMapping("/spots/{spotId}/occupy")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER','ROLE_USER')")
public ResponseEntity<ApiResponse<Void>> markSpotOccupied(
        @PathVariable("spotId") String spotId,
        @RequestParam("occupied") boolean occupied) {
    log.info("Mark spot {} as occupied: {}", spotId, occupied);
    parkingSpotRepository.findById(spotId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Spot not found: " + spotId));
    parkingSpotRepository.updateOccupiedStatus(spotId, occupied);
    return ResponseEntity.ok(
            ApiResponse.success("Spot status updated"));
}
