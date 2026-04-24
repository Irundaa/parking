package com.task.parking.controller;

import java.util.List;
import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.enums.SlotStatus;
import com.task.parking.service.ParkingSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/slots")
@Slf4j
@Tag(name = "Parking Slot Management", description = "Admin endpoints for managing individual parking slots")
public class ParkingSlotController {

  private final ParkingSlotService parkingSlotService;

  @Operation(summary = "Create a parking slot", description = "Adds a new slot to a specific parking level.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Slot successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid request body data")
  })
  @PostMapping
  public ResponseEntity<ParkingSlotResponse> createSlot(@Valid @RequestBody ParkingSlotRequest request) {
    log.info("Creating parking slot");
    ParkingSlotResponse response = parkingSlotService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Get available slots", description = "Retrieves all parking slots with AVAILABLE status.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of available slots")
  @GetMapping("/available")
  public ResponseEntity<List<ParkingSlotResponse>> getAvailableSlots() {
    log.info("Fetching available parking slots");
    return ResponseEntity.status(HttpStatus.OK).body(parkingSlotService.getAvailableSlots());
  }

  @Operation(summary = "Get occupied slots", description = "Retrieves all parking slots with OCCUPIED status.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of occupied slots")
  @GetMapping("/occupied")
  public ResponseEntity<List<ParkingSlotResponse>> getOccupiedSlots() {
    log.info("Fetching occupied parking slots");
    return ResponseEntity.status(HttpStatus.OK).body(parkingSlotService.getOccupiedSlots());
  }

  @Operation(summary = "Change slot status", description = "Manually updates the operational status of a slot.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status successfully updated"),
      @ApiResponse(responseCode = "404", description = "Parking slot not found with the provided ID")
  })
  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> changeStatus(
      @PathVariable("id") Long id,
      @RequestParam("status") SlotStatus status) {
    log.info("Changing status of parking slot with ID: {}", id);
    parkingSlotService.changeStatus(id, status);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Delete a parking slot", description = "Removes a parking slot by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Slot successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Parking slot not found with the provided ID")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSlot(@PathVariable("id") Long id) {
    log.info("Deleting parking slot with ID: {}", id);
    parkingSlotService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
