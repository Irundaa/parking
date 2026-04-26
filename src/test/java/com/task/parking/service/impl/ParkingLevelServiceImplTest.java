package com.task.parking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.entity.ParkingLevel;
import com.task.parking.mapper.ParkingLevelMapper;
import com.task.parking.repository.ParkingLevelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingLevelServiceImplTest {

  private static final Long LEVEL_ID = 1L;
  private static final Long PARKING_LOT_ID = 99L;
  private static final String NOT_FOUND_MESSAGE = "Parking level not found with id: ";
  private static final String NULL_ENTITY_MESSAGE = "Entity must not be null";

  @Mock
  private ParkingLevelRepository parkingLevelRepository;

  @Mock
  private ParkingLevelMapper parkingLevelMapper;

  @InjectMocks
  private ParkingLevelServiceImpl parkingLevelService;

  @Test
  void createShouldSaveLevelAndReturnResponse() {
    ParkingLevelRequest request = buildRequest(PARKING_LOT_ID);
    ParkingLevel entity = buildEntity(null);
    ParkingLevel savedEntity = buildEntity(LEVEL_ID);
    ParkingLevelResponse expectedResponse = buildResponse(LEVEL_ID);

    when(parkingLevelMapper.toEntity(request)).thenReturn(entity);
    when(parkingLevelRepository.save(entity)).thenReturn(savedEntity);
    when(parkingLevelMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

    ParkingLevelResponse actual = parkingLevelService.create(request);

    assertThat(actual).isEqualTo(expectedResponse);
    verify(parkingLevelRepository).save(entity);
  }

  @Test
  void deleteShouldCheckExistenceBeforeDeleting() {
    when(parkingLevelRepository.existsById(LEVEL_ID)).thenReturn(true);

    parkingLevelService.delete(LEVEL_ID);

    InOrder inOrder = inOrder(parkingLevelRepository);
    inOrder.verify(parkingLevelRepository).existsById(LEVEL_ID);
    inOrder.verify(parkingLevelRepository).deleteById(LEVEL_ID);
  }

  @Test
  void deleteShouldThrowExceptionWhenNotFound() {
    when(parkingLevelRepository.existsById(LEVEL_ID)).thenReturn(false);

    assertThatThrownBy(() -> parkingLevelService.delete(LEVEL_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(NOT_FOUND_MESSAGE + LEVEL_ID);

    verify(parkingLevelRepository, never()).deleteById(any());
  }

  private ParkingLevelRequest buildRequest(Long parkingLotId) {
    ParkingLevelRequest request = new ParkingLevelRequest();
    request.setParkingLotId(parkingLotId);
    return request;
  }

  private ParkingLevel buildEntity(Long id) {
    ParkingLevel entity = new ParkingLevel();
    entity.setId(id);
    return entity;
  }

  private ParkingLevelResponse buildResponse(Long id) {
    ParkingLevelResponse response = new ParkingLevelResponse();
    response.setId(id);
    return response;
  }
}
