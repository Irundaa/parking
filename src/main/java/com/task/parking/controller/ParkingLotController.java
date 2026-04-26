package com.task.parking.controller;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.service.ParkingLotService;
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
@RequestMapping(path = "/api/v1/lots")
@Slf4j
@Tag(name = "Parking Lot Management", description = "Admin endpoints for the main parking facility")
public class ParkingLotController {

  private final ParkingLotService parkingLotService;

  @Operation(summary = "Create a parking lot", description = "Initializes a new parking facility.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Parking lot successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid request body data")
  })
  @PostMapping
  public ResponseEntity<ParkingLotResponse> createLot(@Valid @RequestBody ParkingLotRequest request) {
    log.info("Creating parking lot");
    ParkingLotResponse response = parkingLotService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Delete a parking lot", description = "Removes the parking facility entirely.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Parking lot successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Parking lot not found with the provided ID")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLot(@PathVariable("id") Long id) {
    log.info("Deleting parking lot with ID: {}", id);
    parkingLotService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
