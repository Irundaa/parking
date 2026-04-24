package com.task.parking.service;

import java.util.List;
import com.task.parking.dto.CheckInRequest;
import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;

/**
 * Core business logic service for managing the parking process.
 * Handles vehicle check-ins, check-outs, and tracking of active parking sessions.
 */
public interface ParkingService {

  /**
   * Processes a vehicle's entry into the parking lot.
   * Assigns an appropriate available slot based on the vehicle type,
   * creates a new active ticket, and marks the slot as occupied.
   *
   * @param request the check-in request containing the vehicle's license plate and type
   * @return a response containing the assigned slot, entry time, and license plate
   * @throws IllegalArgumentException if the vehicle is already parked (active ticket exists)
   * @throws jakarta.persistence.EntityNotFoundException if no suitable slots are available
   */
  CheckInResponse checkIn(CheckInRequest request);

  /**
   * Processes a vehicle's exit from the parking lot.
   * Calculates the total duration of stay, applies the appropriate fee calculation strategy,
   * completes the active ticket, and frees up the parking slot.
   *
   * @param licensePlate the license plate of the checking-out vehicle
   * @return a summary of the completed session, including total duration and calculated fee
   * @throws jakarta.persistence.EntityNotFoundException if no active session is found for the given license plate
   */
  CheckOutResponse checkOut(String licensePlate);

  /**
   * Retrieves a list of all currently active parking sessions.
   *
   * @return a list of responses representing vehicles currently inside the parking lot
   */
  List<CheckInResponse> getActiveSessions();
}
