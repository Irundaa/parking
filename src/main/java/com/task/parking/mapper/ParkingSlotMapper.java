package com.task.parking.mapper;

import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.entity.ParkingSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between ParkingSlot entity and its corresponding DTOs.
 */
@Mapper(componentModel = "spring")
public interface ParkingSlotMapper {

  /**
   * Converts a ParkingSlotRequest DTO to a ParkingSlot entity.
   *
   * @param request the request DTO containing parking slot details
   * @return the mapped ParkingSlot entity
   */
  @Mapping(source = "parkingLevelId", target = "parkingLevel.id")
  ParkingSlot toEntity(ParkingSlotRequest request);

  /**
   * Converts a ParkingSlot entity to a ParkingSlotResponse DTO.
   *
   * @param entity the ParkingSlot entity
   * @return the mapped ParkingSlotResponse DTO
   */
  @Mapping(source = "parkingLevel.id", target = "parkingLevelId")
  ParkingSlotResponse toResponse(ParkingSlot entity);
}
