package com.task.parking.strategy;

import java.math.BigDecimal;
import com.task.parking.enums.VehicleType;

/**
 * Strategy interface for calculating parking fees.
 * Implementations of this interface define the specific pricing rules
 * and hourly rates for different types of vehicles.
 */
public interface FeeCalculationStrategy {

  /**
   * Calculates the total parking fee based on the duration of the stay.
   *
   * @param duration the total time the vehicle was parked, typically rounded up to the nearest hour
   * @return the calculated parking fee as a {@link BigDecimal}
   */
  BigDecimal calculate(Long duration);

  /**
   * Identifies the specific vehicle type that this pricing strategy applies to.
   * This method is utilized by the strategy factory to automatically map
   * incoming vehicles to their corresponding fee calculators.
   *
   * @return the {@link VehicleType} associated with this specific calculation strategy
   */
  VehicleType getVehicleType();
}
