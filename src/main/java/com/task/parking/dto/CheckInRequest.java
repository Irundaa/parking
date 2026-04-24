package com.task.parking.dto;

import com.task.parking.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {

  @NotBlank(message = "License plate must not be blank")
  @Pattern(regexp = "^[A-Z0-9- ]+$", message = "License plate must contain only uppercase letters, numbers, spaces, or hyphens")
  private String licensePlate;

  @NotNull(message = "Vehicle type is required")
  private VehicleType vehicleType;
}
