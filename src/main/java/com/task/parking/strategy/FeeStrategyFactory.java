package com.task.parking.strategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.task.parking.enums.VehicleType;
import com.task.parking.exception.InvalidParkingRequestException;
import org.springframework.stereotype.Component;

@Component
public class FeeStrategyFactory {

  private final Map<VehicleType, FeeCalculationStrategy> strategies;

  public FeeStrategyFactory(List<FeeCalculationStrategy> strategyList) {
    this.strategies = strategyList.stream()
        .collect(Collectors.toMap(
            FeeCalculationStrategy::getVehicleType,
            strategy -> strategy
        ));
  }

  public FeeCalculationStrategy getStrategy(VehicleType vehicleType) {
    FeeCalculationStrategy strategy = strategies.get(vehicleType);
    if (strategy == null) {
      throw new InvalidParkingRequestException("Unknown vehicle type: " + vehicleType);
    }
    return strategy;
  }
}
