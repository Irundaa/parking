package com.task.parking.mapper;

import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.entity.ParkingSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingSlotMapper {

  @Mapping(source = "parkingLevelId", target = "parkingLevel.id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  ParkingSlot toEntity(ParkingSlotRequest request);

  @Mapping(source = "parkingLevel.id", target = "parkingLevelId")
  ParkingSlotResponse toResponse(ParkingSlot entity);
}
