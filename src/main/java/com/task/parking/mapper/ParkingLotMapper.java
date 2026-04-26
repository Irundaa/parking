package com.task.parking.mapper;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.entity.ParkingLot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingLotMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "parkingLevels", ignore = true)
  ParkingLot toEntity(ParkingLotRequest request);

  ParkingLotResponse toResponse(ParkingLot entity);
}
