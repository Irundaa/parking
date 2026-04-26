package com.task.parking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.entity.ParkingLot;
import com.task.parking.mapper.ParkingLotMapper;
import com.task.parking.repository.ParkingLotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceImplTest {

  private static final Long LOT_ID = 1L;
  private static final String LOT_NAME = "Main Campus Parking";
  private static final String NOT_FOUND_MESSAGE = "Parking lot not found with id: ";
  private static final String NULL_ENTITY_MESSAGE = "Entity must not be null";

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private ParkingLotMapper parkingLotMapper;

  @InjectMocks
  private ParkingLotServiceImpl parkingLotService;

  @Test
  void createShouldSaveLotAndReturnResponse() {
    ParkingLotRequest request = buildRequest(LOT_NAME);
    ParkingLot entity = buildEntity(null, LOT_NAME);
    ParkingLot savedEntity = buildEntity(LOT_ID, LOT_NAME);
    ParkingLotResponse expectedResponse = buildResponse(LOT_ID, LOT_NAME);

    when(parkingLotMapper.toEntity(request)).thenReturn(entity);
    when(parkingLotRepository.save(entity)).thenReturn(savedEntity);
    when(parkingLotMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

    ParkingLotResponse actual = parkingLotService.create(request);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(LOT_ID);
    assertThat(actual.getName()).isEqualTo(LOT_NAME);
    assertThat(actual).isEqualTo(expectedResponse);
    verify(parkingLotRepository).save(entity);
  }

  @Test
  void deleteShouldRemoveLotWhenExists() {
    when(parkingLotRepository.existsById(LOT_ID)).thenReturn(true);

    parkingLotService.delete(LOT_ID);

    verify(parkingLotRepository).deleteById(LOT_ID);
  }

  @Test
  void deleteShouldThrowExceptionWhenNotFound() {
    when(parkingLotRepository.existsById(LOT_ID)).thenReturn(false);

    assertThatThrownBy(() -> parkingLotService.delete(LOT_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(NOT_FOUND_MESSAGE + LOT_ID);

    verify(parkingLotRepository, never()).deleteById(any());
  }

  private ParkingLotRequest buildRequest(String name) {
    ParkingLotRequest request = new ParkingLotRequest();
    request.setName(name);
    return request;
  }

  private ParkingLot buildEntity(Long id, String name) {
    ParkingLot entity = new ParkingLot();
    entity.setId(id);
    entity.setName(name);
    return entity;
  }

  private ParkingLotResponse buildResponse(Long id, String name) {
    ParkingLotResponse response = new ParkingLotResponse();
    response.setId(id);
    response.setName(name);
    return response;
  }
}
