package com.task.parking.mapper;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.entity.ParkingLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between ParkingLevel entity and its corresponding DTOs.
 */
@Mapper(componentModel = "spring")
public interface ParkingLevelMapper {

  /**
   * Converts a ParkingLevelRequest DTO to a ParkingLevel entity.
   *
   * @param request the request DTO containing parking level details
   * @return the mapped ParkingLevel entity
   */
  @Mapping(source = "parkingLotId", target = "parkingLot.id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "parkingSlots", ignore = true)
  ParkingLevel toEntity(ParkingLevelRequest request);

  /**
   * Converts a ParkingLevel entity to a ParkingLevelResponse DTO.
   *
   * @param entity the ParkingLevel entity
   * @return the mapped ParkingLevelResponse DTO
   */
  @Mapping(source = "parkingLot.id", target = "parkingLotId")
  ParkingLevelResponse toResponse(ParkingLevel entity);
}
