package com.task.parking.repository;

import com.task.parking.entity.ParkingLevel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ParkingLevel} entities.
 * Provides standard database operations for parking levels within a parking lot.
 */
public interface ParkingLevelRepository extends JpaRepository<ParkingLevel, Long> {
}
