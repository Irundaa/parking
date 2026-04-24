package com.task.parking.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import com.task.parking.dto.CheckInRequest;
import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.entity.Ticket;
import com.task.parking.entity.Vehicle;
import com.task.parking.enums.SlotStatus;
import com.task.parking.enums.TicketStatus;
import com.task.parking.factory.VehicleFactory;
import com.task.parking.mapper.TicketMapper;
import com.task.parking.repository.TicketRepository;
import com.task.parking.repository.VehicleRepository;
import com.task.parking.service.ParkingService;
import com.task.parking.service.ParkingSlotService;
import com.task.parking.strategy.FeeCalculationStrategy;
import com.task.parking.strategy.FeeStrategyFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingServiceImpl implements ParkingService {

  private final VehicleFactory vehicleFactory;
  private final ParkingSlotService parkingSlotService;
  private final VehicleRepository vehicleRepository;
  private final TicketRepository ticketRepository;
  private final FeeStrategyFactory feeStrategyFactory;
  private final TicketMapper ticketMapper;

  @Override
  @Transactional
  public CheckInResponse checkIn(CheckInRequest request) {
    if (ticketRepository.existsByVehicleLicensePlateAndTicketStatus(request.getLicensePlate(),TicketStatus.ACTIVE)){
      throw new IllegalArgumentException("Ticket already exists");
    }

    Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
        .orElseGet(() -> {
          Vehicle newVehicle = vehicleFactory.createVehicle(request.getLicensePlate(), request.getVehicleType());
          return vehicleRepository.save(newVehicle);
        });

    ParkingSlot parkingSlot = parkingSlotService.getAvailableSlot(vehicle.getAllowedSlotTypes());
    parkingSlot.setStatus(SlotStatus.OCCUPIED);

    Ticket ticket = new Ticket();
    ticket.setVehicle(vehicle);
    ticket.setParkingSlot(parkingSlot);
    ticket.setEntryTime(LocalDateTime.now());
    ticket.setTicketStatus(TicketStatus.ACTIVE);

    ticket = ticketRepository.save(ticket);

    CheckInResponse response = new CheckInResponse();
    response.setLicensePlate(request.getLicensePlate());
    response.setParkingSlotId(parkingSlot.getId());
    response.setEntryTime(ticket.getEntryTime());
    response.setTicketId(ticket.getId());

    log.info("Vehicle {} checked in successfully on slot {}. Ticket ID: {}",
        vehicle.getLicensePlate(), parkingSlot.getId(), ticket.getId());

    return response;
  }

  @Override
  @Transactional
  public CheckOutResponse checkOut(String licensePlate) {
    Ticket ticket = ticketRepository.findByVehicleLicensePlateAndTicketStatus(licensePlate,
        TicketStatus.ACTIVE).orElseThrow(() -> new EntityNotFoundException(
            "Parking session with license plate: " + licensePlate + " does not exist" ));

    ParkingSlot parkingSlot = ticket.getParkingSlot();
    parkingSlot.setStatus(SlotStatus.AVAILABLE);

    ticket.setExitTime(LocalDateTime.now());

    Duration duration = Duration.between(ticket.getEntryTime(), ticket.getExitTime());
    long hours = duration.toHours();
    if (duration.toMinutesPart() > 0) hours++;
    String durationString = String.format("%d hours %d minutes", duration.toHours(),
        duration.toMinutesPart());

    FeeCalculationStrategy strategy = feeStrategyFactory.getStrategy(ticket
        .getVehicle().getVehicleType());
    BigDecimal fee = strategy.calculate(hours);

    ticket.setTicketStatus(TicketStatus.COMPLETED);
    ticket.setFee(fee);
    ticket = ticketRepository.save(ticket);

    CheckOutResponse response = new CheckOutResponse();
    response.setLicensePlate(licensePlate);
    response.setEntryTime(ticket.getEntryTime());
    response.setExitTime(ticket.getExitTime());
    response.setTicketId(ticket.getId());
    response.setDuration(durationString);
    response.setFee(fee);

    log.info("Vehicle {} checked out successfully. Duration: {}. Fee: {}. Slot {} is now available.",
        licensePlate, durationString, fee, parkingSlot.getId());

    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CheckInResponse> getActiveSessions() {
    log.info("Fetching all active parking sessions...");

    List<CheckInResponse> activeSessions = ticketRepository.findAllByTicketStatus(TicketStatus.ACTIVE)
        .stream()
        .map(ticketMapper::toCheckInResponse)
        .toList();

    log.info("Successfully fetched {} active parking sessions", activeSessions.size());

    return activeSessions;
  }
}
