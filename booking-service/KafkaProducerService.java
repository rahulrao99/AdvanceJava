package com.parking.booking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String BOOKING_CREATED   = "booking.created";
    public static final String BOOKING_CANCELLED = "booking.cancelled";
    public static final String VEHICLE_ENTRY     = "vehicle.entry";
    public static final String VEHICLE_EXIT      = "vehicle.exit";

    public void publishBookingCreated(BookingEvent event) {
        log.info("Publishing booking.created event: {}", 
                 event.getBookingId());
        kafkaTemplate.send(BOOKING_CREATED, 
                event.getBookingId(), event);
    }

    public void publishBookingCancelled(BookingEvent event) {
        log.info("Publishing booking.cancelled event: {}", 
                 event.getBookingId());
        kafkaTemplate.send(BOOKING_CANCELLED, 
                event.getBookingId(), event);
    }

    public void publishVehicleEntry(BookingEvent event) {
        log.info("Publishing vehicle.entry event: {}", 
                 event.getBookingId());
        kafkaTemplate.send(VEHICLE_ENTRY, 
                event.getBookingId(), event);
    }

    public void publishVehicleExit(BookingEvent event) {
        log.info("Publishing vehicle.exit event: {}", 
                 event.getBookingId());
        kafkaTemplate.send(VEHICLE_EXIT, 
                event.getBookingId(), event);
    }
}
