public void updateOccupiedStatus(String spotId, boolean isOccupied) {
    String sql = """
            UPDATE parking_spots
            SET is_occupied = ?, updated_at = NOW()
            WHERE id = ?
            """;
    jdbcTemplate.update(sql, isOccupied, spotId);
}
