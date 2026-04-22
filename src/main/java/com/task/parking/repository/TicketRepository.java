package com.task.parking.repository;

import java.util.List;
import java.util.Optional;
import com.task.parking.entity.Ticket;
import com.task.parking.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
  List<Ticket> findAllByStatus(Status status);
  Optional<Ticket> findByVehicleLicensePlateAndStatus(String licensePlate, Status status);
}
