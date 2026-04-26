package com.task.parking.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.task.parking.entity.Car;
import com.task.parking.entity.Motorcycle;
import com.task.parking.entity.Truck;
import com.task.parking.entity.Vehicle;
import com.task.parking.enums.VehicleType;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VehicleFactoryTest {

  private static final String LICENSE_PLATE = "AB1234CD";
  private static final String NULL_TYPE_MSG = "Vehicle type must not be null";
  private static final String NULL_PLATE_MSG = "License plate must not be null";

  private final VehicleFactory vehicleFactory = new VehicleFactory();

  @ParameterizedTest
  @MethodSource("vehicleTypeToClassMappings")
  void createVehicleShouldReturnCorrectVehicleInstanceWithLicensePlate(
      VehicleType type, Class<? extends Vehicle> expectedClass) {

    Vehicle actual = vehicleFactory.createVehicle(LICENSE_PLATE, type);

    assertThat(actual).isInstanceOf(expectedClass);
    assertThat(actual.getLicensePlate()).isEqualTo(LICENSE_PLATE);
  }

  @Test
  void createVehicleShouldThrowExceptionWhenTypeIsNull() {
    assertThatThrownBy(() -> vehicleFactory.createVehicle(LICENSE_PLATE, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(NULL_TYPE_MSG);
  }

  @Test
  void createVehicleShouldThrowExceptionWhenLicensePlateIsNull() {
    assertThatThrownBy(() -> vehicleFactory.createVehicle(null, VehicleType.CAR))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(NULL_PLATE_MSG);
  }

  private static Stream<Arguments> vehicleTypeToClassMappings() {
    return Stream.of(
        Arguments.of(VehicleType.CAR, Car.class),
        Arguments.of(VehicleType.TRUCK, Truck.class),
        Arguments.of(VehicleType.MOTORCYCLE, Motorcycle.class)
    );
  }
}
