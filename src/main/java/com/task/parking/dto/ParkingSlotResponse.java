package com.task.parking.dto;

import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotResponse {
  private Long id;
  private ParkingSlotType type;
  private SlotStatus status;
  private Long parkingLevelId;
}
