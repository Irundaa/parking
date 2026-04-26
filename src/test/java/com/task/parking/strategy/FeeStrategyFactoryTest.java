package com.task.parking.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.parking.enums.VehicleType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeStrategyFactoryTest {

  private static final String UNKNOWN_TYPE_MSG = "Unknown vehicle type: ";

  private FeeCalculationStrategy carStrategy;
  private FeeCalculationStrategy truckStrategy;
  private FeeCalculationStrategy motorcycleStrategy;

  private FeeStrategyFactory feeStrategyFactory;

  @BeforeEach
  void setUp() {
    carStrategy = mock(FeeCalculationStrategy.class);
    truckStrategy = mock(FeeCalculationStrategy.class);
    motorcycleStrategy = mock(FeeCalculationStrategy.class);

    when(carStrategy.getVehicleType()).thenReturn(VehicleType.CAR);
    when(truckStrategy.getVehicleType()).thenReturn(VehicleType.TRUCK);
    when(motorcycleStrategy.getVehicleType()).thenReturn(VehicleType.MOTORCYCLE);

    feeStrategyFactory = new FeeStrategyFactory(List.of(carStrategy, truckStrategy, motorcycleStrategy));
  }

  @Test
  void getStrategyShouldReturnCorrectStrategyWhenTypeIsValid() {
    FeeCalculationStrategy actualCarStrategy = feeStrategyFactory.getStrategy(VehicleType.CAR);
    FeeCalculationStrategy actualTruckStrategy = feeStrategyFactory.getStrategy(VehicleType.TRUCK);
    FeeCalculationStrategy actualMotorcycleStrategy = feeStrategyFactory.getStrategy(VehicleType.MOTORCYCLE);

    assertThat(actualCarStrategy).isEqualTo(carStrategy);
    assertThat(actualTruckStrategy).isEqualTo(truckStrategy);
    assertThat(actualMotorcycleStrategy).isEqualTo(motorcycleStrategy);
  }

  @Test
  void getStrategyShouldThrowExceptionWhenTypeIsMissing() {
    FeeStrategyFactory incompleteFactory = new FeeStrategyFactory(List.of(carStrategy));

    assertThatThrownBy(() -> incompleteFactory.getStrategy(VehicleType.TRUCK))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(UNKNOWN_TYPE_MSG + VehicleType.TRUCK);
  }

  @Test
  void getStrategyShouldThrowExceptionWhenTypeIsNull() {
    assertThatThrownBy(() -> feeStrategyFactory.getStrategy(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(UNKNOWN_TYPE_MSG + "null");
  }
}
