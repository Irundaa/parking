package com.task.parking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotRequest {

  @NotBlank(message = "Parking lot name must not be blank")
  private String name;
}
