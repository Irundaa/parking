package com.task.parking.dto;

import com.task.parking.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
  private String licensePlate;
  private VehicleType vehicleType;
}
