package com.task.parking.mapper;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.entity.ParkingLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingLevelMapper {

  @Mapping(source = "parkingLotId", target = "parkingLot.id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "parkingSlots", ignore = true)
  ParkingLevel toEntity(ParkingLevelRequest request);

  @Mapping(source = "parkingLot.id", target = "parkingLotId")
  ParkingLevelResponse toResponse(ParkingLevel entity);
}
