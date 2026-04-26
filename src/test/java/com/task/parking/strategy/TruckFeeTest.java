package com.task.parking.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.task.parking.enums.VehicleType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TruckFeeTest {

  private final TruckFee truckFee = new TruckFee();

  @ParameterizedTest
  @CsvSource({
      "0, 0",
      "1, 3",
      "5, 15",
      "24, 72"
  })
  void calculateShouldReturnCorrectFee(Long duration, long expectedFee) {
    BigDecimal expected = BigDecimal.valueOf(expectedFee);

    BigDecimal actual = truckFee.calculate(duration);

    assertThat(actual).isEqualByComparingTo(expected);
  }

  @Test
  void getVehicleTypeShouldReturnTruck() {
    VehicleType actual = truckFee.getVehicleType();

    assertThat(actual).isEqualTo(VehicleType.TRUCK);
  }
}
