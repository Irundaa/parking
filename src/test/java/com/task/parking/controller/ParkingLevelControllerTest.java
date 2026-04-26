package com.task.parking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.exception.InvalidParkingRequestException;
import com.task.parking.exception.ResourceNotFoundException;
import com.task.parking.service.ParkingLevelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ParkingLevelControllerTest {

  private static final Long LEVEL_ID = 1L;
  private static final Long PARKING_LOT_ID = 99L;

  @Mock
  private ParkingLevelService parkingLevelService;

  @InjectMocks
  private ParkingLevelController parkingLevelController;

  @Test
  void createLevelShouldReturnCreatedStatusWhenValidRequest() {
    ParkingLevelRequest request = buildRequest(PARKING_LOT_ID);
    ParkingLevelResponse expectedResponse = buildResponse(LEVEL_ID);
    when(parkingLevelService.create(request)).thenReturn(expectedResponse);

    ResponseEntity<ParkingLevelResponse> actual = parkingLevelController.createLevel(request);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getBody()).isNotNull();
    assertThat(actual.getBody().getId()).isEqualTo(LEVEL_ID);
    verify(parkingLevelService).create(request);
  }

  @Test
  void createLevelShouldThrowExceptionWhenServiceFails() {
    ParkingLevelRequest request = buildRequest(PARKING_LOT_ID);
    when(parkingLevelService.create(request))
        .thenThrow(new InvalidParkingRequestException("Invalid parking level data"));

    assertThatThrownBy(() -> parkingLevelController.createLevel(request))
        .isInstanceOf(InvalidParkingRequestException.class)
        .hasMessageContaining("Invalid parking level data");
  }

  @Test
  void deleteLevelShouldReturnNoContentStatusWhenLevelExists() {
    ResponseEntity<Void> actual = parkingLevelController.deleteLevel(LEVEL_ID);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(actual.getBody()).isNull();
    verify(parkingLevelService).delete(LEVEL_ID);
    verifyNoMoreInteractions(parkingLevelService);
  }

  @Test
  void deleteLevelShouldThrowExceptionWhenLevelNotFound() {
    doThrow(new ResourceNotFoundException("Level not found"))
        .when(parkingLevelService).delete(LEVEL_ID);

    assertThatThrownBy(() -> parkingLevelController.deleteLevel(LEVEL_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("not found");
  }

  private ParkingLevelRequest buildRequest(Long parkingLotId) {
    ParkingLevelRequest request = new ParkingLevelRequest();
    request.setParkingLotId(parkingLotId);
    return request;
  }

  private ParkingLevelResponse buildResponse(Long id) {
    ParkingLevelResponse response = new ParkingLevelResponse();
    response.setId(id);
    return response;
  }
}
