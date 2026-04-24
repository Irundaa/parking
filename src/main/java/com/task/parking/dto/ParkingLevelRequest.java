package com.task.parking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLevelRequest {

  @NotNull(message = "Parking lot ID is required")
  @Positive(message = "Parking lot ID must be a positive number")
  private Long parkingLotId;
}
