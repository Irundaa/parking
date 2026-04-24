package com.task.parking.repository;

import java.util.Optional;
import com.task.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Vehicle} entities.
 * Provides access to the registered vehicles in the system.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

  Optional<Vehicle> findByLicensePlate(String licensePlate);
}
