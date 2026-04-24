package com.task.parking.repository;

import com.task.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing the {@link ParkingLot} entity.
 * Acts as the root data access component for the entire parking facility.
 */
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
}
