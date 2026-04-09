package com.parking.booking.repository;

import com.parking.booking.model.VehicleSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VehicleSessionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<VehicleSession> rowMapper = (rs, rowNum) ->
            VehicleSession.builder()
                    .id(rs.getString("id"))
                    .bookingId(rs.getString("booking_id"))
                    .entryGateId(rs.getString("entry_gate_id"))
                    .exitGateId(rs.getString("exit_gate_id"))
                    .vehicleNumber(rs.getString("vehicle_number"))
                    .entryTime(rs.getTimestamp("entry_time")
                            .toLocalDateTime())
                    .exitTime(rs.getTimestamp("exit_time") != null
                            ? rs.getTimestamp("exit_time").toLocalDateTime()
                            : null)
                    .durationMinutes(rs.getBigDecimal("duration_minutes"))
                    .feeCharged(rs.getBigDecimal("fee_charged"))
                    .build();

    public void save(VehicleSession session) {
        log.debug("Saving vehicle session: {}", session.getId());
        String sql = """
                INSERT INTO vehicle_sessions
                (id, booking_id, entry_gate_id, vehicle_number, entry_time)
                VALUES (?, ?, ?, ?, NOW())
                """;
        jdbcTemplate.update(sql,
                session.getId(),
                session.getBookingId(),
                session.getEntryGateId(),
                session.getVehicleNumber()
        );
    }

    public Optional<VehicleSession> findByBookingId(String bookingId) {
        String sql = """
                SELECT * FROM vehicle_sessions
                WHERE booking_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, bookingId)
                .stream().findFirst();
    }

    public void updateExit(String bookingId, String exitGateId,
                            java.math.BigDecimal durationMinutes,
                            java.math.BigDecimal feeCharged) {
        String sql = """
                UPDATE vehicle_sessions
                SET exit_gate_id = ?, exit_time = NOW(),
                    duration_minutes = ?, fee_charged = ?
                WHERE booking_id = ?
                """;
        jdbcTemplate.update(sql, exitGateId, 
                durationMinutes, feeCharged, bookingId);
    }
}
