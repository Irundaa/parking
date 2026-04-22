package com.task.parking.entity;

import java.util.List;
import com.task.parking.enums.ParkingSlotType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("TRUCK")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Truck extends Vehicle {

  @Override
  @Transient
  public List<ParkingSlotType> getAllowedSlotTypes() {
    return List.of(ParkingSlotType.LARGE);
  }
}
