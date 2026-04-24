package com.task.parking.factory;

import com.task.parking.entity.Car;
import com.task.parking.entity.Motorcycle;
import com.task.parking.entity.Truck;
import com.task.parking.entity.Vehicle;
import com.task.parking.enums.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class VehicleFactory {

  public Vehicle createVehicle(String licensePlate, VehicleType type) {
    Vehicle vehicle = switch (type) {
      case CAR -> new Car();
      case TRUCK -> new Truck();
      case MOTORCYCLE -> new Motorcycle();
    };
    vehicle.setLicensePlate(licensePlate);
    return vehicle;
  }
}
