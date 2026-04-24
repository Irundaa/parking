package com.task.parking.entity;

import java.util.List;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.VehicleType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class Vehicle {
  @Id
  private String licensePlate;

  @Transient
  public abstract List<ParkingSlotType> getAllowedSlotTypes();

  @Transient
  public abstract VehicleType getVehicleType();
}
