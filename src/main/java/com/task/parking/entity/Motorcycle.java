package com.task.parking.entity;

import java.util.List;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.VehicleType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("MOTORCYCLE")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Motorcycle extends Vehicle {

  @Override
  @Transient
  public List<ParkingSlotType> getAllowedSlotTypes() {
    return List.of(ParkingSlotType.COMPACT, ParkingSlotType.MOTORCYCLE);
  }

  @Override
  @Transient
  public VehicleType getVehicleType() {
    return VehicleType.MOTORCYCLE;
  }
}
