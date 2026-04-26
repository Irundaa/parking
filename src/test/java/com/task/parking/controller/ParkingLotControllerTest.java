package com.task.parking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.service.ParkingLotService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ParkingLotControllerTest {

  private static final Long LOT_ID = 1L;
  private static final String LOT_NAME = "Main Parking Lot";

  @Mock
  private ParkingLotService parkingLotService;

  @InjectMocks
  private ParkingLotController parkingLotController;

  @Test
  void createLotShouldReturnCreatedStatusWhenValidRequest() {
    ParkingLotRequest request = buildRequest(LOT_NAME);
    ParkingLotResponse expectedResponse = buildResponse(LOT_ID, LOT_NAME);
    when(parkingLotService.create(request)).thenReturn(expectedResponse);

    ResponseEntity<ParkingLotResponse> actual = parkingLotController.createLot(request);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getBody()).isNotNull();
    assertThat(actual.getBody().getId()).isEqualTo(LOT_ID);
    assertThat(actual.getBody().getName()).isEqualTo(LOT_NAME);
    verify(parkingLotService).create(request);
  }

  @Test
  void createLotShouldThrowExceptionWhenServiceFails() {
    ParkingLotRequest request = buildRequest(LOT_NAME);
    when(parkingLotService.create(request))
        .thenThrow(new IllegalArgumentException("Invalid parking lot data"));

    assertThatThrownBy(() -> parkingLotController.createLot(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid parking lot data");
  }

  @Test
  void deleteLotShouldReturnNoContentStatusWhenLotExists() {
    ResponseEntity<Void> actual = parkingLotController.deleteLot(LOT_ID);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(actual.getBody()).isNull();
    verify(parkingLotService).delete(LOT_ID);
    verifyNoMoreInteractions(parkingLotService);
  }

  @Test
  void deleteLotShouldThrowExceptionWhenLotNotFound() {
    doThrow(new EntityNotFoundException("Lot not found"))
        .when(parkingLotService).delete(LOT_ID);

    assertThatThrownBy(() -> parkingLotController.deleteLot(LOT_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("not found");
  }

  private ParkingLotRequest buildRequest(String name) {
    ParkingLotRequest request = new ParkingLotRequest();
    request.setName(name);
    return request;
  }

  private ParkingLotResponse buildResponse(Long id, String name) {
    ParkingLotResponse response = new ParkingLotResponse();
    response.setId(id);
    response.setName(name);
    return response;
  }
}
