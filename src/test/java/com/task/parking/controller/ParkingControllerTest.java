package com.task.parking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.parking.dto.CheckInRequest;
import com.task.parking.dto.CheckInResponse;
import com.task.parking.dto.CheckOutResponse;
import com.task.parking.exception.InvalidParkingRequestException;
import com.task.parking.exception.ResourceNotFoundException;
import com.task.parking.service.ParkingService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ParkingControllerTest {

  private static final String LICENSE_PLATE = "AA1111BB";

  @Mock
  private ParkingService parkingService;

  @InjectMocks
  private ParkingController parkingController;

  @Test
  void checkInShouldReturnCreatedStatusWhenValidRequest() {
    CheckInRequest request = buildCheckInRequest(LICENSE_PLATE);
    CheckInResponse expectedResponse = buildCheckInResponse(LICENSE_PLATE);
    when(parkingService.checkIn(request)).thenReturn(expectedResponse);

    ResponseEntity<CheckInResponse> actual = parkingController.checkIn(request);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getBody()).isEqualTo(expectedResponse);
    assertThat(actual.getBody().getLicensePlate()).isEqualTo(LICENSE_PLATE);
    verify(parkingService).checkIn(request);
  }

  @Test
  void checkInShouldThrowExceptionWhenCarAlreadyParked() {
    CheckInRequest request = buildCheckInRequest(LICENSE_PLATE);
    when(parkingService.checkIn(request))
        .thenThrow(new InvalidParkingRequestException("Ticket already exists"));

    assertThatThrownBy(() -> parkingController.checkIn(request))
        .isInstanceOf(InvalidParkingRequestException.class)
        .hasMessageContaining("already exists");
  }

  @Test
  void checkOutShouldReturnOkStatusWhenCarExists() {
    CheckOutResponse expectedResponse = buildCheckOutResponse(LICENSE_PLATE);
    when(parkingService.checkOut(LICENSE_PLATE)).thenReturn(expectedResponse);

    ResponseEntity<CheckOutResponse> actual = parkingController.checkOut(LICENSE_PLATE);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody()).isNotNull();
    assertThat(actual.getBody().getLicensePlate()).isEqualTo(LICENSE_PLATE);
    verify(parkingService).checkOut(LICENSE_PLATE);
  }

  @Test
  void checkOutShouldThrowExceptionWhenCarNotFound() {
    when(parkingService.checkOut(LICENSE_PLATE))
        .thenThrow(new ResourceNotFoundException("Parking session with license plate does not exist"));

    assertThatThrownBy(() -> parkingController.checkOut(LICENSE_PLATE))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("does not exist");
  }

  @Test
  void getActiveSessionsShouldReturnOkStatusAndListWhenSessionsExist() {
    List<CheckInResponse> expectedSessions = List.of(
        buildCheckInResponse("AA1111BB"),
        buildCheckInResponse("BB2222CC")
    );
    when(parkingService.getActiveSessions()).thenReturn(expectedSessions);

    ResponseEntity<List<CheckInResponse>> actual = parkingController.getActiveSessions();

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody())
        .hasSize(2)
        .extracting(CheckInResponse::getLicensePlate)
        .containsExactly("AA1111BB", "BB2222CC");
  }

  @Test
  void getActiveSessionsShouldReturnEmptyListWhenNoActiveSessions() {
    when(parkingService.getActiveSessions()).thenReturn(Collections.emptyList());

    ResponseEntity<List<CheckInResponse>> actual = parkingController.getActiveSessions();

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody()).isEmpty();
  }

  private CheckInRequest buildCheckInRequest(String plate) {
    CheckInRequest request = new CheckInRequest();
    request.setLicensePlate(plate);
    return request;
  }

  private CheckInResponse buildCheckInResponse(String plate) {
    CheckInResponse response = new CheckInResponse();
    response.setLicensePlate(plate);
    return response;
  }

  private CheckOutResponse buildCheckOutResponse(String plate) {
    CheckOutResponse response = new CheckOutResponse();
    response.setLicensePlate(plate);
    return response;
  }
}
