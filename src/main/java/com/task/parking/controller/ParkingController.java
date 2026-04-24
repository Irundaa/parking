package com.task.parking.controller;

import java.util.List;
import com.task.parking.dto.CheckInRequest;
import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;
import com.task.parking.service.ParkingService;
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
@RequestMapping(path = "/api/v1/parking")
@Slf4j
@Tag(name = "Parking Management", description = "Endpoints for vehicle check-in, check-out, and active sessions tracking")
public class ParkingController {

  private final ParkingService parkingService;

  @Operation(summary = "Check-in a vehicle", description = "Creates a new parking session and assigns an available slot.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Vehicle successfully checked in"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "404", description = "No available slots found"),
      @ApiResponse(responseCode = "409", description = "Vehicle already has an active session")
  })
  @PostMapping("/check-in")
  public ResponseEntity<CheckInResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
    log.info("CheckIn request: {}", request.getLicensePlate());
    CheckInResponse checkInResponse = parkingService.checkIn(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(checkInResponse);
  }

  @Operation(summary = "Check-out a vehicle", description = "Completes a parking session, frees the slot, and calculates the fee.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vehicle successfully checked out"),
      @ApiResponse(responseCode = "404", description = "Active parking session not found for the given license plate")
  })
  @PostMapping("/check-out/{licensePlate}")
  public ResponseEntity<CheckOutResponse> checkOut(@PathVariable("licensePlate") String licensePlate) {
    log.info("CheckOut request: {}", licensePlate);
    CheckOutResponse checkOutResponse = parkingService.checkOut(licensePlate);
    return ResponseEntity.status(HttpStatus.OK).body(checkOutResponse);
  }

  @Operation(summary = "Get active sessions", description = "Retrieves a list of all vehicles currently parked.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved active sessions")
  @GetMapping("/sessions/active")
  public ResponseEntity<List<CheckInResponse>> getActiveSessions() {
    log.info("Get active sessions");
    List<CheckInResponse> activeSessions = parkingService.getActiveSessions();
    return ResponseEntity.status(HttpStatus.OK).body(activeSessions);
  }
}
