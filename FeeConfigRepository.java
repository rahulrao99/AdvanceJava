package com.parking.admin.repository;

import com.parking.admin.model.FeeConfig;
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
public class FeeConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<FeeConfig> rowMapper = (rs, rowNum) ->
            FeeConfig.builder()
                    .id(rs.getString("id"))
                    .lotId(rs.getString("lot_id"))
                    .vehicleType(rs.getString("vehicle_type"))
                    .ratePerHour(rs.getBigDecimal("rate_per_hour"))
                    .graceMinutes(rs.getInt("grace_minutes"))
                    .isActive(rs.getBoolean("is_active"))
                    .createdAt(rs.getTimestamp("created_at")
                            .toLocalDateTime())
                    .build();

    public void save(FeeConfig feeConfig) {
        log.debug("Saving fee config for lot: {}", feeConfig.getLotId());
        String sql = """
                INSERT INTO fee_config
                (id, lot_id, vehicle_type, rate_per_hour,
                 grace_minutes, is_active, created_at)
                VALUES (?, ?, ?, ?, ?, ?, NOW())
                """;
        jdbcTemplate.update(sql,
                feeConfig.getId(),
                feeConfig.getLotId(),
                feeConfig.getVehicleType(),
                feeConfig.getRatePerHour(),
                feeConfig.getGraceMinutes(),
                feeConfig.isActive()
        );
    }

    public Optional<FeeConfig> findById(String id) {
        String sql = "SELECT * FROM fee_config WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream().findFirst();
    }

    public List<FeeConfig> findByLotId(String lotId) {
        String sql = """
                SELECT * FROM fee_config
                WHERE lot_id = ? AND is_active = true
                """;
        return jdbcTemplate.query(sql, rowMapper, lotId);
    }

    public void update(FeeConfig feeConfig) {
        String sql = """
                UPDATE fee_config
                SET rate_per_hour = ?, grace_minutes = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                feeConfig.getRatePerHour(),
                feeConfig.getGraceMinutes(),
                feeConfig.getId()
        );
    }

    public boolean existsByLotIdAndVehicleType(String lotId,
                                               String vehicleType) {
        String sql = """
                SELECT COUNT(*) FROM fee_config
                WHERE lot_id = ? AND vehicle_type = ?
                """;
        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, lotId, vehicleType);
        return count != null && count > 0;
    }
}
