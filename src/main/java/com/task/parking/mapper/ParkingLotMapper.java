package com.task.parking.mapper;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.entity.ParkingLot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between ParkingLot entity and its corresponding DTOs.
 */
@Mapper(componentModel = "spring")
public interface ParkingLotMapper {

  /**
   * Converts a ParkingLotRequest DTO to a ParkingLot entity.
   *
   * @param request the request DTO containing parking lot details
   * @return the mapped ParkingLot entity
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "parkingLevels", ignore = true)
  ParkingLot toEntity(ParkingLotRequest request);

  /**
   * Converts a ParkingLot entity to a ParkingLotResponse DTO.
   *
   * @param entity the ParkingLot entity
   * @return the mapped ParkingLotResponse DTO
   */
  ParkingLotResponse toResponse(ParkingLot entity);
}
