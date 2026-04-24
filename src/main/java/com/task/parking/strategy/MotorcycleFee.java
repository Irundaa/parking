package com.task.parking.strategy;

import java.math.BigDecimal;
import com.task.parking.enums.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class MotorcycleFee implements FeeCalculationStrategy {

  private static final BigDecimal HOURLY_RATE = BigDecimal.valueOf(1);

  @Override
  public BigDecimal calculate(Long duration) {
    return HOURLY_RATE.multiply(BigDecimal.valueOf(duration));
  }

  @Override
  public VehicleType getVehicleType() {
    return VehicleType.MOTORCYCLE;
  }
}
