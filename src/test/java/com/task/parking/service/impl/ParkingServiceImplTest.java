package com.task.parking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.parking.dto.CheckInRequest;
import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.entity.Ticket;
import com.task.parking.entity.Vehicle;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
import com.task.parking.enums.TicketStatus;
import com.task.parking.enums.VehicleType;
import com.task.parking.exception.ParkingConflictException;
import com.task.parking.exception.ResourceNotFoundException;
import com.task.parking.factory.VehicleFactory;
import com.task.parking.mapper.TicketMapper;
import com.task.parking.repository.TicketRepository;
import com.task.parking.repository.VehicleRepository;
import com.task.parking.service.ParkingSlotService;
import com.task.parking.strategy.FeeCalculationStrategy;
import com.task.parking.strategy.FeeStrategyFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingServiceImplTest {

  private static final String LICENSE_PLATE = "AB1234CD";
  private static final String TICKET_EXISTS_MSG = "Ticket already exists";
  private static final String SESSION_NOT_FOUND_MSG = "Parking session with license plate: " + LICENSE_PLATE + " does not exist";
  private static final Long SLOT_ID = 5L;
  private static final Long TICKET_ID = 10L;
  private static final BigDecimal CALCULATED_FEE = BigDecimal.valueOf(150);

  private static final long PARKED_HOURS = 2L;
  private static final long PARKED_MINUTES = 30L;
  private static final long ROUNDED_HOURS = 3L;
  private static final String EXPECTED_DURATION_STRING = "2 hours 30 minutes";
  private static final String EXPECTED_EXACT_DURATION_STRING = "2 hours 0 minutes";

  @Mock
  private VehicleFactory vehicleFactory;

  @Mock
  private ParkingSlotService parkingSlotService;

  @Mock
  private VehicleRepository vehicleRepository;

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private FeeStrategyFactory feeStrategyFactory;

  @Mock
  private TicketMapper ticketMapper;

  @Mock
  private FeeCalculationStrategy feeCalculationStrategy;

  @InjectMocks
  private ParkingServiceImpl parkingService;

  @Test
  void checkInShouldCreateVehicleAndTicketWhenVehicleNotFound() {
    CheckInRequest request = buildCheckInRequest(LICENSE_PLATE, VehicleType.CAR);
    Vehicle newVehicle = buildVehicle(LICENSE_PLATE);
    ParkingSlot slot = buildParkingSlot(SLOT_ID, SlotStatus.AVAILABLE);
    Ticket savedTicket = buildTicket(TICKET_ID, newVehicle, slot);

    when(ticketRepository.existsByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(false);
    when(vehicleRepository.findByLicensePlate(LICENSE_PLATE)).thenReturn(Optional.empty());
    when(vehicleFactory.createVehicle(LICENSE_PLATE, VehicleType.CAR)).thenReturn(newVehicle);
    when(vehicleRepository.save(newVehicle)).thenReturn(newVehicle);
    when(parkingSlotService.getAvailableSlot(newVehicle.getAllowedSlotTypes())).thenReturn(slot);
    when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

    CheckInResponse actual = parkingService.checkIn(request);

    assertThat(actual).isNotNull();
    assertThat(actual.getLicensePlate()).isEqualTo(LICENSE_PLATE);
    assertThat(actual.getParkingSlotId()).isEqualTo(SLOT_ID);
    assertThat(actual.getTicketId()).isEqualTo(TICKET_ID);
    assertThat(slot.getStatus()).isEqualTo(SlotStatus.OCCUPIED);
    verify(ticketRepository).save(any(Ticket.class));
  }

  @Test
  void checkInShouldUseExistingVehicleWhenVehicleFound() {
    CheckInRequest request = buildCheckInRequest(LICENSE_PLATE, VehicleType.CAR);
    Vehicle existingVehicle = buildVehicle(LICENSE_PLATE);
    ParkingSlot slot = buildParkingSlot(SLOT_ID, SlotStatus.AVAILABLE);
    Ticket savedTicket = buildTicket(TICKET_ID, existingVehicle, slot);

    when(ticketRepository.existsByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(false);
    when(vehicleRepository.findByLicensePlate(LICENSE_PLATE)).thenReturn(Optional.of(existingVehicle));
    when(parkingSlotService.getAvailableSlot(existingVehicle.getAllowedSlotTypes())).thenReturn(slot);
    when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

    CheckInResponse actual = parkingService.checkIn(request);

    assertThat(actual).isNotNull();
    assertThat(actual.getTicketId()).isEqualTo(TICKET_ID);
    verify(vehicleFactory, never()).createVehicle(any(), any());
    verify(vehicleRepository, never()).save(any());
  }

  @Test
  void checkInShouldThrowExceptionWhenTicketExists() {
    CheckInRequest request = buildCheckInRequest(LICENSE_PLATE, VehicleType.CAR);

    when(ticketRepository.existsByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(true);

    assertThatThrownBy(() -> parkingService.checkIn(request))
        .isInstanceOf(ParkingConflictException.class)
        .hasMessageContaining(TICKET_EXISTS_MSG);

    verify(vehicleRepository, never()).findByLicensePlate(any());
  }

  @Test
  void checkInShouldThrowExceptionWhenRequestIsNull() {
    assertThatThrownBy(() -> parkingService.checkIn(null))
        .isInstanceOf(NullPointerException.class);

    verify(ticketRepository, never()).save(any());
  }

  @Test
  void checkOutShouldCalculateFeeAndReturnResponseWhenSuccessful() {
    Vehicle vehicle = buildVehicle(LICENSE_PLATE);
    ParkingSlot slot = buildParkingSlot(SLOT_ID, SlotStatus.OCCUPIED);
    Ticket ticket = buildTicket(TICKET_ID, vehicle, slot);
    ticket.setEntryTime(LocalDateTime.now().minusHours(2).minusMinutes(30));

    when(ticketRepository.findByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(Optional.of(ticket));
    when(feeStrategyFactory.getStrategy(vehicle.getVehicleType())).thenReturn(feeCalculationStrategy);
    when(feeCalculationStrategy.calculate(anyLong())).thenReturn(CALCULATED_FEE);
    when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

    CheckOutResponse actual = parkingService.checkOut(LICENSE_PLATE);

    assertThat(actual).isNotNull();
    assertThat(actual.getLicensePlate()).isEqualTo(LICENSE_PLATE);
    assertThat(actual.getFee()).isEqualTo(CALCULATED_FEE);
    assertThat(actual.getTicketId()).isEqualTo(TICKET_ID);
    assertThat(slot.getStatus()).isEqualTo(SlotStatus.AVAILABLE);
    assertThat(ticket.getTicketStatus()).isEqualTo(TicketStatus.COMPLETED);
    assertThat(ticket.getExitTime()).isNotNull();
    verify(ticketRepository).save(ticket);
  }

  @Test
  void checkOutShouldThrowExceptionWhenTicketNotFound() {
    when(ticketRepository.findByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> parkingService.checkOut(LICENSE_PLATE))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(SESSION_NOT_FOUND_MSG);

    verify(feeStrategyFactory, never()).getStrategy(any());
    verify(ticketRepository, never()).save(any());
  }

  @Test
  void getActiveSessionsShouldReturnListOfResponses() {
    Vehicle vehicle = buildVehicle(LICENSE_PLATE);
    ParkingSlot slot = buildParkingSlot(SLOT_ID, SlotStatus.OCCUPIED);
    Ticket ticket = buildTicket(TICKET_ID, vehicle, slot);
    CheckInResponse response = buildCheckInResponse(LICENSE_PLATE, SLOT_ID, TICKET_ID);

    when(ticketRepository.findAllByTicketStatus(TicketStatus.ACTIVE)).thenReturn(List.of(ticket));
    when(ticketMapper.toCheckInResponse(ticket)).thenReturn(response);

    List<CheckInResponse> actual = parkingService.getActiveSessions();

    assertThat(actual).hasSize(1).containsExactly(response);
    verify(ticketRepository).findAllByTicketStatus(TicketStatus.ACTIVE);
  }

  @Test
  void getActiveSessionsShouldReturnEmptyListWhenNoSessions() {
    when(ticketRepository.findAllByTicketStatus(TicketStatus.ACTIVE)).thenReturn(Collections.emptyList());

    List<CheckInResponse> actual = parkingService.getActiveSessions();

    assertThat(actual).isEmpty();
  }

  @Test
  void checkOutShouldCalculateFeeWithRoundedHoursWhenMinutesExist() {
    Vehicle vehicle = buildVehicle(LICENSE_PLATE);
    ParkingSlot slot = buildParkingSlot(SLOT_ID, SlotStatus.OCCUPIED);
    Ticket ticket = buildTicket(TICKET_ID, vehicle, slot);

    ticket.setEntryTime(LocalDateTime.now().minusHours(PARKED_HOURS).minusMinutes(PARKED_MINUTES));

    when(ticketRepository.findByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(Optional.of(ticket));
    when(feeStrategyFactory.getStrategy(vehicle.getVehicleType())).thenReturn(feeCalculationStrategy);

    when(feeCalculationStrategy.calculate(ROUNDED_HOURS)).thenReturn(CALCULATED_FEE);
    when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

    CheckOutResponse actual = parkingService.checkOut(LICENSE_PLATE);

    assertThat(actual).isNotNull();
    assertThat(actual.getFee()).isEqualTo(CALCULATED_FEE);
    assertThat(actual.getDuration()).isEqualTo(EXPECTED_DURATION_STRING);
    assertThat(slot.getStatus()).isEqualTo(SlotStatus.AVAILABLE);
    assertThat(ticket.getTicketStatus()).isEqualTo(TicketStatus.COMPLETED);
    verify(feeCalculationStrategy).calculate(ROUNDED_HOURS);
  }

  @Test
  void checkOutShouldCalculateFeeWithoutRoundingWhenExactHours() {
    Vehicle vehicle = buildVehicle(LICENSE_PLATE);
    ParkingSlot slot = buildParkingSlot(SLOT_ID, SlotStatus.OCCUPIED);
    Ticket ticket = buildTicket(TICKET_ID, vehicle, slot);

    ticket.setEntryTime(LocalDateTime.now().minusHours(PARKED_HOURS));

    when(ticketRepository.findByVehicleLicensePlateAndTicketStatus(LICENSE_PLATE, TicketStatus.ACTIVE))
        .thenReturn(Optional.of(ticket));
    when(feeStrategyFactory.getStrategy(vehicle.getVehicleType())).thenReturn(feeCalculationStrategy);

    when(feeCalculationStrategy.calculate(PARKED_HOURS)).thenReturn(CALCULATED_FEE);
    when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

    CheckOutResponse actual = parkingService.checkOut(LICENSE_PLATE);

    assertThat(actual.getDuration()).isEqualTo(EXPECTED_EXACT_DURATION_STRING);
    verify(feeCalculationStrategy).calculate(PARKED_HOURS);
  }

  private CheckInRequest buildCheckInRequest(String licensePlate, VehicleType type) {
    CheckInRequest request = new CheckInRequest();
    request.setLicensePlate(licensePlate);
    request.setVehicleType(type);
    return request;
  }

  private CheckInResponse buildCheckInResponse(String licensePlate, Long slotId, Long ticketId) {
    CheckInResponse response = new CheckInResponse();
    response.setLicensePlate(licensePlate);
    response.setParkingSlotId(slotId);
    response.setTicketId(ticketId);
    response.setEntryTime(LocalDateTime.now());
    return response;
  }

  private Vehicle buildVehicle(String licensePlate) {
    Vehicle vehicle = new Vehicle() {
      @Override
      public List<ParkingSlotType> getAllowedSlotTypes() {
        return List.of(ParkingSlotType.COMPACT);
      }

      @Override
      public VehicleType getVehicleType() {
        return VehicleType.CAR;
      }
    };
    vehicle.setLicensePlate(licensePlate);
    return vehicle;
  }

  private ParkingSlot buildParkingSlot(Long id, SlotStatus status) {
    ParkingSlot slot = new ParkingSlot();
    slot.setId(id);
    slot.setStatus(status);
    return slot;
  }

  private Ticket buildTicket(Long id, Vehicle vehicle, ParkingSlot slot) {
    Ticket ticket = new Ticket();
    ticket.setId(id);
    ticket.setVehicle(vehicle);
    ticket.setParkingSlot(slot);
    ticket.setTicketStatus(TicketStatus.ACTIVE);
    ticket.setEntryTime(LocalDateTime.now());
    return ticket;
  }
}
