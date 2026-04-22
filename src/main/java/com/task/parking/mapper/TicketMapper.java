package com.task.parking.mapper;

import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;
import com.task.parking.dto.TicketResponse;
import com.task.parking.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting Ticket entity to various response DTOs.
 */
@Mapper(componentModel = "spring")
public interface TicketMapper {

  /**
   * Converts a Ticket entity to a CheckInResponse DTO.
   *
   * @param ticket the Ticket entity
   * @return the mapped CheckInResponse DTO
   */
  @Mapping(source = "id", target = "ticketId")
  @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
  @Mapping(source = "parkingSlot.id", target = "parkingSlotId")
  CheckInResponse toCheckInResponse(Ticket ticket);

  /**
   * Converts a Ticket entity to a CheckOutResponse DTO.
   *
   * @param ticket the Ticket entity
   * @return the mapped CheckOutResponse DTO
   */
  @Mapping(source = "id", target = "ticketId")
  @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
  CheckOutResponse toCheckOutResponse(Ticket ticket);

  /**
   * Converts a Ticket entity to a comprehensive TicketResponse DTO.
   *
   * @param ticket the Ticket entity
   * @return the mapped TicketResponse DTO
   */
  @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
  @Mapping(source = "parkingSlot.id", target = "parkingSlotId")
  TicketResponse toResponse(Ticket ticket);
}
