package com.task.parking.repository;

import com.task.parking.entity.ParkingLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLevelRepository extends JpaRepository<ParkingLevel, Long> {
}
