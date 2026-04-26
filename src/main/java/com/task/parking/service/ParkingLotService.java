package com.task.parking.service;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.exception.ResourceNotFoundException;

/**
 * Service interface for managing the parking lot entity.
 * Handles the creation and deletion of the main parking facility.
 */
public interface ParkingLotService {

  /**
   * Creates a new parking lot based on the provided request data.
   *
   * @param request the data transfer object containing parking lot details
   * @return a response object containing the created parking lot information
   */
  ParkingLotResponse create(ParkingLotRequest request);

  /**
   * Deletes a parking lot by its unique identifier.
   *
   * @param id the unique identifier of the parking lot to delete
   * @throws ResourceNotFoundException if the lot ID does not exist
   */
  void delete(Long id);
}
