package com.parking.admin.repository;

import com.parking.admin.model.ParkingSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParkingSpotRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ParkingSpot> rowMapper = (rs, rowNum) ->
            ParkingSpot.builder()
                    .id(rs.getString("id"))
                    .levelId(rs.getString("level_id"))
                    .spotCode(rs.getString("spot_code"))
                    .vehicleType(rs.getString("vehicle_type"))
                    .isOccupied(rs.getBoolean("is_occupied"))
                    .isReserved(rs.getBoolean("is_reserved"))
                    .isActive(rs.getBoolean("is_active"))
                    .build();

    public void save(ParkingSpot spot) {
        log.debug("Saving parking spot: {}", spot.getSpotCode());
        String sql = """
                INSERT INTO parking_spots
                (id, level_id, spot_code, vehicle_type,
                 is_occupied, is_reserved, is_active, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
                """;
        jdbcTemplate.update(sql,
                spot.getId(),
                spot.getLevelId(),
                spot.getSpotCode(),
                spot.getVehicleType(),
                spot.isOccupied(),
                spot.isReserved(),
                spot.isActive()
        );
    }

    public Optional<ParkingSpot> findById(String id) {
        String sql = "SELECT * FROM parking_spots WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream().findFirst();
    }

    public List<ParkingSpot> findByLevelId(String levelId) {
        log.debug("Finding spots for level: {}", levelId);
        String sql = """
                SELECT * FROM parking_spots
                WHERE level_id = ?
                ORDER BY spot_code ASC
                """;
        return jdbcTemplate.query(sql, rowMapper, levelId);
    }

    public void update(ParkingSpot spot) {
        log.debug("Updating spot: {}", spot.getId());
        String sql = """
                UPDATE parking_spots
                SET spot_code = ?, vehicle_type = ?,
                    is_active = ?, updated_at = NOW()
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                spot.getSpotCode(),
                spot.getVehicleType(),
                spot.isActive(),
                spot.getId()
        );
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM parking_spots WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByLevelIdAndSpotCode(String levelId,
                                              String spotCode) {
        String sql = """
                SELECT COUNT(*) FROM parking_spots
                WHERE level_id = ? AND spot_code = ?
                """;
        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, levelId, spotCode);
        return count != null && count > 0;
    }
}
