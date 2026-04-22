package com.task.parking.repository;

import java.util.List;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.enums.ParkingSlotType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
  List<ParkingSlot> findByIsAvailableTrueAndTypeIn(List<ParkingSlotType> allowedTypes);
}
