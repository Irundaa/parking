package com.task.parking.dto;

import com.task.parking.enums.ParkingSlotType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotResponse {
  private Long id;
  private ParkingSlotType type;
  private boolean isAvailable;
  private Long parkingLevelId;
}
