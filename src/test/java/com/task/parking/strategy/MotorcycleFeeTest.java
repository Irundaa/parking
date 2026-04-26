package com.task.parking.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.task.parking.enums.VehicleType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MotorcycleFeeTest {

  private final MotorcycleFee motorcycleFee = new MotorcycleFee();

  @ParameterizedTest
  @CsvSource({
      "0, 0",
      "1, 1",
      "5, 5",
      "24, 24"
  })
  void calculateShouldReturnCorrectFee(Long duration, long expectedFee) {
    BigDecimal expected = BigDecimal.valueOf(expectedFee);

    BigDecimal actual = motorcycleFee.calculate(duration);

    assertThat(actual).isEqualByComparingTo(expected);
  }

  @Test
  void getVehicleTypeShouldReturnMotorcycle() {
    VehicleType actual = motorcycleFee.getVehicleType();

    assertThat(actual).isEqualTo(VehicleType.MOTORCYCLE);
  }
}
