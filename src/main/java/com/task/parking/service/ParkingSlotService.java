package com.task.parking.service;

import java.util.List;
import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;

/**
 * Service interface for managing individual parking slots.
 * Provides operations for retrieving, creating, updating, and deleting slots.
 */
public interface ParkingSlotService {

  /**
   * Creates a new parking slot.
   *
   * @param request the data transfer object containing slot details
   * @return a response object containing the created slot information
   */
  ParkingSlotResponse create(ParkingSlotRequest request);

  /**
   * Retrieves all parking slots that are currently available for parking.
   *
   * @return a list of available parking slots
   */
  List<ParkingSlotResponse> getAvailableSlots();

  /**
   * Retrieves all parking slots that are currently occupied.
   *
   * @return a list of occupied parking slots
   */
  List<ParkingSlotResponse> getOccupiedSlots();

  /**
   * Finds the first available parking slot that matches any of the allowed slot types.
   * This method is primarily used internally during the check-in process.
   *
   * @param allowedTypes a list of slot types suitable for a specific vehicle
   * @return the first available parking slot entity
   * @throws jakarta.persistence.EntityNotFoundException if no matching available slot is found
   */
  ParkingSlot getAvailableSlot(List<ParkingSlotType> allowedTypes);

  /**
   * Manually changes the operational status of a specific parking slot.
   *
   * @param id the unique identifier of the parking slot
   * @param status the new status to apply (e.g., MAINTENANCE, AVAILABLE)
   * @throws jakarta.persistence.EntityNotFoundException if the slot ID does not exist
   */
  void changeStatus(Long id, SlotStatus status);

  /**
   * Deletes a parking slot by its unique identifier.
   *
   * @param id the unique identifier of the parking slot to delete
   * @throws jakarta.persistence.EntityNotFoundException if the slot ID does not exist
   */
  void delete(Long id);
}
