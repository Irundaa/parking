package com.task.parking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
import com.task.parking.exception.InvalidParkingRequestException;
import com.task.parking.exception.ResourceNotFoundException;
import com.task.parking.service.ParkingSlotService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ParkingSlotControllerTest {

  private static final Long SLOT_ID = 1L;
  private static final Long SECOND_SLOT_ID = 2L;
  private static final Long LEVEL_ID = 5L;

  @Mock
  private ParkingSlotService parkingSlotService;

  @InjectMocks
  private ParkingSlotController parkingSlotController;

  @Test
  void createSlotShouldReturnCreatedStatusWhenValidRequest() {
    ParkingSlotRequest request = buildRequest(LEVEL_ID);
    ParkingSlotResponse expectedResponse = buildResponse(SLOT_ID, SlotStatus.AVAILABLE);
    when(parkingSlotService.create(request)).thenReturn(expectedResponse);

    ResponseEntity<ParkingSlotResponse> actual = parkingSlotController.createSlot(request);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getBody()).isNotNull();
    assertThat(actual.getBody().getId()).isEqualTo(SLOT_ID);
    assertThat(actual.getBody().getStatus()).isEqualTo(SlotStatus.AVAILABLE);
    verify(parkingSlotService).create(request);
  }

  @Test
  void createSlotShouldThrowExceptionWhenServiceFails() {
    ParkingSlotRequest request = buildRequest(LEVEL_ID);
    when(parkingSlotService.create(request))
        .thenThrow(new InvalidParkingRequestException("Invalid parking slot data"));

    assertThatThrownBy(() -> parkingSlotController.createSlot(request))
        .isInstanceOf(InvalidParkingRequestException.class)
        .hasMessageContaining("Invalid parking slot data");
  }

  @Test
  void getAvailableSlotsShouldReturnOkStatusAndListWhenSlotsExist() {
    List<ParkingSlotResponse> expectedSlots = List.of(
        buildResponse(SLOT_ID, SlotStatus.AVAILABLE),
        buildResponse(SECOND_SLOT_ID, SlotStatus.AVAILABLE)
    );
    when(parkingSlotService.getAvailableSlots()).thenReturn(expectedSlots);

    ResponseEntity<List<ParkingSlotResponse>> actual = parkingSlotController.getAvailableSlots();

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody())
        .hasSize(2)
        .extracting(ParkingSlotResponse::getStatus)
        .containsOnly(SlotStatus.AVAILABLE);
    verify(parkingSlotService).getAvailableSlots();
  }

  @Test
  void getAvailableSlotsShouldReturnEmptyListWhenNoSlotsAvailable() {
    when(parkingSlotService.getAvailableSlots()).thenReturn(Collections.emptyList());

    ResponseEntity<List<ParkingSlotResponse>> actual = parkingSlotController.getAvailableSlots();

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody()).isEmpty();
  }

  @Test
  void getOccupiedSlotsShouldReturnOkStatusAndListWhenSlotsExist() {
    List<ParkingSlotResponse> expectedSlots = List.of(
        buildResponse(SLOT_ID, SlotStatus.OCCUPIED),
        buildResponse(SECOND_SLOT_ID, SlotStatus.OCCUPIED)
    );
    when(parkingSlotService.getOccupiedSlots()).thenReturn(expectedSlots);

    ResponseEntity<List<ParkingSlotResponse>> actual = parkingSlotController.getOccupiedSlots();

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody())
        .hasSize(2)
        .extracting(ParkingSlotResponse::getStatus)
        .containsOnly(SlotStatus.OCCUPIED);
    verify(parkingSlotService).getOccupiedSlots();
  }

  @Test
  void getOccupiedSlotsShouldReturnEmptyListWhenNoOccupiedSlots() {
    when(parkingSlotService.getOccupiedSlots()).thenReturn(Collections.emptyList());

    ResponseEntity<List<ParkingSlotResponse>> actual = parkingSlotController.getOccupiedSlots();

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody()).isEmpty();
  }

  @Test
  void changeStatusShouldReturnOkStatusWhenValidIdAndStatus() {
    ResponseEntity<Void> actual = parkingSlotController.changeStatus(SLOT_ID, SlotStatus.OCCUPIED);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody()).isNull();
    verify(parkingSlotService).changeStatus(SLOT_ID, SlotStatus.OCCUPIED);
    verifyNoMoreInteractions(parkingSlotService);
  }

  @Test
  void changeStatusShouldThrowExceptionWhenSlotNotFound() {
    doThrow(new ResourceNotFoundException("Slot not found"))
        .when(parkingSlotService).changeStatus(SLOT_ID, SlotStatus.OCCUPIED);

    assertThatThrownBy(() -> parkingSlotController.changeStatus(SLOT_ID, SlotStatus.OCCUPIED))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("not found");
  }

  @ParameterizedTest
  @EnumSource(SlotStatus.class)
  void changeStatusShouldWorkForAllStatuses(SlotStatus status) {
    ResponseEntity<Void> actual = parkingSlotController.changeStatus(SLOT_ID, status);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(parkingSlotService).changeStatus(SLOT_ID, status);
  }

  @Test
  void deleteSlotShouldReturnNoContentStatusWhenSlotExists() {
    ResponseEntity<Void> actual = parkingSlotController.deleteSlot(SLOT_ID);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(actual.getBody()).isNull();
    verify(parkingSlotService).delete(SLOT_ID);
    verifyNoMoreInteractions(parkingSlotService);
  }

  @Test
  void deleteSlotShouldThrowExceptionWhenSlotNotFound() {
    doThrow(new ResourceNotFoundException("Slot not found"))
        .when(parkingSlotService).delete(SLOT_ID);

    assertThatThrownBy(() -> parkingSlotController.deleteSlot(SLOT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("not found");
  }

  private ParkingSlotRequest buildRequest(Long parkingLevelId) {
    ParkingSlotRequest request = new ParkingSlotRequest();
    request.setParkingLevelId(parkingLevelId);
    request.setType(ParkingSlotType.COMPACT);
    return request;
  }

  private ParkingSlotResponse buildResponse(Long id, SlotStatus status) {
    ParkingSlotResponse response = new ParkingSlotResponse();
    response.setId(id);
    response.setStatus(status);
    return response;
  }
}
