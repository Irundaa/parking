package com.task.parking.mapper;

import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;
import com.task.parking.dto.TicketResponse;
import com.task.parking.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

  @Mapping(source = "id", target = "ticketId")
  @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
  @Mapping(source = "parkingSlot.id", target = "parkingSlotId")
  CheckInResponse toCheckInResponse(Ticket ticket);

  @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
  @Mapping(source = "parkingSlot.id", target = "parkingSlotId")
  TicketResponse toResponse(Ticket ticket);
}
