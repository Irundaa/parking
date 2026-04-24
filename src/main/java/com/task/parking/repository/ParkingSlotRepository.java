package com.task.parking.repository;

import java.util.List;
import java.util.Optional;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ParkingSlot} entities.
 * Provides specialized methods to search for slots based on their operational status and supported vehicle types.
 */
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

  List<ParkingSlot> findByStatusAndTypeIn(SlotStatus status, List<ParkingSlotType> allowedTypes);

  Optional<ParkingSlot> findFirstByStatusAndTypeIn(SlotStatus status, List<ParkingSlotType> allowedTypes);

  List<ParkingSlot> findAllByStatus(SlotStatus status);
}
