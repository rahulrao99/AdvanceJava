package com.parking.admin.repository;

import com.parking.admin.model.ParkingLevel;
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
public class ParkingLevelRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ParkingLevel> rowMapper = (rs, rowNum) ->
            ParkingLevel.builder()
                    .id(rs.getString("id"))
                    .lotId(rs.getString("lot_id"))
                    .levelNumber(rs.getInt("level_number"))
                    .label(rs.getString("label"))
                    .totalSpots(rs.getInt("total_spots"))
                    .build();

    public void save(ParkingLevel level) {
        log.debug("Saving parking level: {}", level.getLabel());
        String sql = """
                INSERT INTO parking_levels
                (id, lot_id, level_number, label, total_spots)
                VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql,
                level.getId(),
                level.getLotId(),
                level.getLevelNumber(),
                level.getLabel(),
                level.getTotalSpots()
        );
    }

    public Optional<ParkingLevel> findById(String id) {
        String sql = "SELECT * FROM parking_levels WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream().findFirst();
    }

    public List<ParkingLevel> findByLotId(String lotId) {
        log.debug("Finding levels for lot: {}", lotId);
        String sql = """
                SELECT * FROM parking_levels
                WHERE lot_id = ?
                ORDER BY level_number ASC
                """;
        return jdbcTemplate.query(sql, rowMapper, lotId);
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM parking_levels WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByLotIdAndLevelNumber(String lotId,
                                               Integer levelNumber) {
        String sql = """
                SELECT COUNT(*) FROM parking_levels
                WHERE lot_id = ? AND level_number = ?
                """;
        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, lotId, levelNumber);
        return count != null && count > 0;
    }
}
