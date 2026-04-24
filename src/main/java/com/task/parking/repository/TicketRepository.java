package com.task.parking.repository;

import java.util.List;
import java.util.Optional;
import com.task.parking.entity.Ticket;
import com.task.parking.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Ticket} entities.
 * Handles database operations for tracking active and completed parking sessions.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  List<Ticket> findAllByTicketStatus(TicketStatus ticketStatus);

  Optional<Ticket> findByVehicleLicensePlateAndTicketStatus(String licensePlate, TicketStatus ticketStatus);

  boolean existsByVehicleLicensePlateAndTicketStatus(String licensePlate, TicketStatus ticketStatus);
}
