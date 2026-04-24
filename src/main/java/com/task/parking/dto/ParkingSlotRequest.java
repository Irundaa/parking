package com.task.parking.dto;

import com.task.parking.enums.ParkingSlotType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotRequest {

  @NotNull(message = "Parking slot type is required")
  private ParkingSlotType type;

  @NotNull(message = "Parking level ID is required")
  @Positive(message = "Parking level ID must be a positive number")
  private Long parkingLevelId;
}
