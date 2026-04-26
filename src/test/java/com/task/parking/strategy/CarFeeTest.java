package com.task.parking.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.task.parking.enums.VehicleType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CarFeeTest {

  private final CarFee carFee = new CarFee();

  @ParameterizedTest
  @CsvSource({
      "0, 0",
      "1, 2",
      "5, 10",
      "24, 48"
  })
  void calculateShouldReturnCorrectFee(Long duration, long expectedFee) {
    BigDecimal expected = BigDecimal.valueOf(expectedFee);

    BigDecimal actual = carFee.calculate(duration);

    assertThat(actual).isEqualByComparingTo(expected);
  }

  @Test
  void getVehicleTypeShouldReturnCar() {
    VehicleType actual = carFee.getVehicleType();

    assertThat(actual).isEqualTo(VehicleType.CAR);
  }
}
