package com.task.parking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
import com.task.parking.mapper.ParkingSlotMapper;
import com.task.parking.repository.ParkingSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingSlotServiceImplTest {

  private static final Long SLOT_ID = 1L;
  private static final Long LEVEL_ID = 5L;
  private static final String NOT_FOUND_BY_ID_MSG = "Parking slot not found with ID: ";
  private static final String NOT_FOUND_BY_TYPE_MSG = "Parking slot not found for this type";
  private static final String NULL_ENTITY_MESSAGE = "Entity must not be null";

  @Mock
  private ParkingSlotRepository parkingSlotRepository;

  @Mock
  private ParkingSlotMapper parkingSlotMapper;

  @InjectMocks
  private ParkingSlotServiceImpl parkingSlotService;

  @Test
  void createShouldSaveSlotAndReturnResponse() {
    ParkingSlotRequest request = buildRequest(LEVEL_ID, ParkingSlotType.COMPACT);
    ParkingSlot entity = buildEntity(null, null);
    ParkingSlot savedEntity = buildEntity(SLOT_ID, SlotStatus.AVAILABLE);
    ParkingSlotResponse expectedResponse = buildResponse(SLOT_ID, SlotStatus.AVAILABLE);

    when(parkingSlotMapper.toEntity(request)).thenReturn(entity);
    when(parkingSlotRepository.save(entity)).thenReturn(savedEntity);
    when(parkingSlotMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

    ParkingSlotResponse actual = parkingSlotService.create(request);

    assertThat(actual).isEqualTo(expectedResponse);
    verify(parkingSlotRepository).save(entity);
  }

  @Test
  void getAvailableSlotsShouldReturnListOfResponsesWithCorrectStatus() {
    ParkingSlot entity = buildEntity(SLOT_ID, SlotStatus.AVAILABLE);
    ParkingSlotResponse response = buildResponse(SLOT_ID, SlotStatus.AVAILABLE);

    when(parkingSlotRepository.findAllByStatus(SlotStatus.AVAILABLE)).thenReturn(List.of(entity));
    when(parkingSlotMapper.toResponse(entity)).thenReturn(response);

    List<ParkingSlotResponse> actual = parkingSlotService.getAvailableSlots();

    assertThat(actual)
        .hasSize(1)
        .extracting(ParkingSlotResponse::getStatus)
        .containsOnly(SlotStatus.AVAILABLE);
    verify(parkingSlotRepository).findAllByStatus(SlotStatus.AVAILABLE);
  }

  @Test
  void getAvailableSlotsShouldReturnEmptyListWhenNoSlots() {
    when(parkingSlotRepository.findAllByStatus(SlotStatus.AVAILABLE)).thenReturn(Collections.emptyList());

    List<ParkingSlotResponse> actual = parkingSlotService.getAvailableSlots();

    assertThat(actual).isEmpty();
  }

  @Test
  void getOccupiedSlotsShouldReturnListOfResponsesWithCorrectStatus() {
    ParkingSlot entity = buildEntity(SLOT_ID, SlotStatus.OCCUPIED);
    ParkingSlotResponse response = buildResponse(SLOT_ID, SlotStatus.OCCUPIED);

    when(parkingSlotRepository.findAllByStatus(SlotStatus.OCCUPIED)).thenReturn(List.of(entity));
    when(parkingSlotMapper.toResponse(entity)).thenReturn(response);

    List<ParkingSlotResponse> actual = parkingSlotService.getOccupiedSlots();

    assertThat(actual)
        .hasSize(1)
        .extracting(ParkingSlotResponse::getStatus)
        .containsOnly(SlotStatus.OCCUPIED);
    verify(parkingSlotRepository).findAllByStatus(SlotStatus.OCCUPIED);
  }

  @Test
  void getOccupiedSlotsShouldReturnEmptyListWhenNoSlots() {
    when(parkingSlotRepository.findAllByStatus(SlotStatus.OCCUPIED)).thenReturn(Collections.emptyList());

    List<ParkingSlotResponse> actual = parkingSlotService.getOccupiedSlots();

    assertThat(actual).isEmpty();
  }

  @Test
  void getAvailableSlotShouldReturnSlotWhenFound() {
    List<ParkingSlotType> allowedTypes = List.of(ParkingSlotType.COMPACT, ParkingSlotType.LARGE);
    ParkingSlot expectedSlot = buildEntity(SLOT_ID, SlotStatus.AVAILABLE);

    when(parkingSlotRepository.findFirstByStatusAndTypeIn(SlotStatus.AVAILABLE, allowedTypes))
        .thenReturn(Optional.of(expectedSlot));

    ParkingSlot actual = parkingSlotService.getAvailableSlot(allowedTypes);

    assertThat(actual).isEqualTo(expectedSlot);
    verify(parkingSlotRepository).findFirstByStatusAndTypeIn(SlotStatus.AVAILABLE, allowedTypes);
  }

  @Test
  void getAvailableSlotShouldThrowExceptionWhenNotFound() {
    List<ParkingSlotType> allowedTypes = List.of(ParkingSlotType.MOTORCYCLE);

    when(parkingSlotRepository.findFirstByStatusAndTypeIn(SlotStatus.AVAILABLE, allowedTypes))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> parkingSlotService.getAvailableSlot(allowedTypes))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(NOT_FOUND_BY_TYPE_MSG);
  }

  @ParameterizedTest
  @CsvSource({
      "AVAILABLE, OCCUPIED",
      "OCCUPIED, AVAILABLE"
  })
  void changeStatusShouldUpdateStatusForAllTransitions(SlotStatus from, SlotStatus to) {
    ParkingSlot slot = buildEntity(SLOT_ID, from);

    when(parkingSlotRepository.findById(SLOT_ID)).thenReturn(Optional.of(slot));

    parkingSlotService.changeStatus(SLOT_ID, to);

    assertThat(slot.getStatus()).isEqualTo(to);
    verify(parkingSlotRepository).findById(SLOT_ID);
  }

  @Test
  void changeStatusShouldThrowExceptionWhenNotFound() {
    when(parkingSlotRepository.findById(SLOT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> parkingSlotService.changeStatus(SLOT_ID, SlotStatus.OCCUPIED))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(NOT_FOUND_BY_ID_MSG + SLOT_ID);
  }

  @Test
  void deleteShouldRemoveSlotWhenExists() {
    when(parkingSlotRepository.existsById(SLOT_ID)).thenReturn(true);

    parkingSlotService.delete(SLOT_ID);

    verify(parkingSlotRepository).deleteById(SLOT_ID);
  }

  @Test
  void deleteShouldThrowExceptionWhenNotFound() {
    when(parkingSlotRepository.existsById(SLOT_ID)).thenReturn(false);

    assertThatThrownBy(() -> parkingSlotService.delete(SLOT_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(NOT_FOUND_BY_ID_MSG + SLOT_ID);

    verify(parkingSlotRepository, never()).deleteById(any());
  }

  private ParkingSlotRequest buildRequest(Long levelId, ParkingSlotType type) {
    ParkingSlotRequest request = new ParkingSlotRequest();
    request.setParkingLevelId(levelId);
    request.setType(type);
    return request;
  }

  private ParkingSlot buildEntity(Long id, SlotStatus status) {
    ParkingSlot entity = new ParkingSlot();
    entity.setId(id);
    entity.setStatus(status);
    return entity;
  }

  private ParkingSlotResponse buildResponse(Long id, SlotStatus status) {
    ParkingSlotResponse response = new ParkingSlotResponse();
    response.setId(id);
    response.setStatus(status);
    return response;
  }
}
