package com.task.parking.controller;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.service.ParkingLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/levels")
@Slf4j
@Tag(name = "Parking Level Management", description = "Admin endpoints for managing parking levels (floors)")
public class ParkingLevelController {

  private final ParkingLevelService parkingLevelService;

  @Operation(summary = "Create a parking level", description = "Adds a new level to the parking lot.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Level successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid request body data")
  })
  @PostMapping
  public ResponseEntity<ParkingLevelResponse> createLevel(@Valid @RequestBody ParkingLevelRequest request) {
    log.info("Creating parking level");
    ParkingLevelResponse response = parkingLevelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Delete a parking level", description = "Removes a parking level.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Level successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Parking level not found with the provided ID"),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLevel(@PathVariable("id") Long id) {
    log.info("Deleting parking level with ID: {}", id);
    parkingLevelService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
