package com.task.parking.service;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.exception.ResourceNotFoundException;

/**
 * Service interface for managing parking levels.
 * Provides operations to create and delete parking levels within a parking lot.
 */
public interface ParkingLevelService {

  /**
   * Creates a new parking level based on the provided request data.
   *
   * @param request the data transfer object containing parking level details
   * @return a response object containing the created parking level information
   */
  ParkingLevelResponse create(ParkingLevelRequest request);

  /**
   * Deletes a parking level by its unique identifier.
   *
   * @param id the unique identifier of the parking level to delete
   * @throws ResourceNotFoundException if the level ID does not exist
   */
  void delete(Long id);
}
