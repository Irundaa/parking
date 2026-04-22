package com.task.parking.dto;

import com.task.parking.enums.ParkingSlotType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotRequest {
  private ParkingSlotType type;
  private Long parkingLevelId;
}
