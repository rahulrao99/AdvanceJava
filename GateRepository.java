package com.parking.admin.repository;

import com.parking.admin.model.EntryExitGate;
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
public class GateRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<EntryExitGate> rowMapper = (rs, rowNum) ->
            EntryExitGate.builder()
                    .id(rs.getString("id"))
                    .lotId(rs.getString("lot_id"))
                    .gateType(rs.getString("gate_type"))
                    .gateCode(rs.getString("gate_code"))
                    .latitude(rs.getDouble("latitude"))
                    .longitude(rs.getDouble("longitude"))
                    .isActive(rs.getBoolean("is_active"))
                    .createdAt(rs.getTimestamp("created_at")
                            .toLocalDateTime())
                    .build();

    public void save(EntryExitGate gate) {
        log.debug("Saving gate: {}", gate.getGateCode());
        String sql = """
                INSERT INTO entry_exit_gates
                (id, lot_id, gate_type, gate_code,
                 latitude, longitude, is_active, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
                """;
        jdbcTemplate.update(sql,
                gate.getId(),
                gate.getLotId(),
                gate.getGateType(),
                gate.getGateCode(),
                gate.getLatitude(),
                gate.getLongitude(),
                gate.isActive()
        );
    }

    public Optional<EntryExitGate> findById(String id) {
        String sql = "SELECT * FROM entry_exit_gates WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream().findFirst();
    }

    public List<EntryExitGate> findByLotId(String lotId) {
        log.debug("Finding gates for lot: {}", lotId);
        String sql = """
                SELECT * FROM entry_exit_gates
                WHERE lot_id = ? AND is_active = true
                ORDER BY gate_code ASC
                """;
        return jdbcTemplate.query(sql, rowMapper, lotId);
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM entry_exit_gates WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByLotIdAndGateCode(String lotId,
                                            String gateCode) {
        String sql = """
                SELECT COUNT(*) FROM entry_exit_gates
                WHERE lot_id = ? AND gate_code = ?
                """;
        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, lotId, gateCode);
        return count != null && count > 0;
    }
}
