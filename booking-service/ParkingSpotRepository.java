public void updateOccupiedStatus(String spotId, boolean isOccupied) {
    String sql = """
            UPDATE parking_spots
            SET is_occupied = ?, updated_at = NOW()
            WHERE id = ?
            """;
    jdbcTemplate.update(sql, isOccupied, spotId);
}




public String findLotIdBySpotId(String spotId) {
    String sql = """
            SELECT pl.lot_id
            FROM parking_spots ps
            JOIN parking_levels pl ON ps.level_id = pl.id
            WHERE ps.id = ?
            """;
    return jdbcTemplate.queryForObject(sql, String.class, spotId);
}





@GetMapping("/spots/{spotId}/lot")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOT_MANAGER','ROLE_USER')")
public ResponseEntity<ApiResponse<String>> getLotIdBySpot(
        @PathVariable("spotId") String spotId) {
    String lotId = parkingSpotRepository.findLotIdBySpotId(spotId);
    return ResponseEntity.ok(ApiResponse.success("Lot ID fetched", lotId));
}
