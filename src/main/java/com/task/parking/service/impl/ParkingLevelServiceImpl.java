package com.task.parking.service.impl;

import com.task.parking.dto.ParkingLevelRequest;
import com.task.parking.dto.ParkingLevelResponse;
import com.task.parking.entity.ParkingLevel;
import com.task.parking.exception.ResourceNotFoundException;
import com.task.parking.mapper.ParkingLevelMapper;
import com.task.parking.repository.ParkingLevelRepository;
import com.task.parking.service.ParkingLevelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingLevelServiceImpl implements ParkingLevelService {

  private final ParkingLevelRepository parkingLevelRepository;
  private final ParkingLevelMapper parkingLevelMapper;

  @Override
  @Transactional
  public ParkingLevelResponse create(ParkingLevelRequest request) {
    ParkingLevel parkingLevel = parkingLevelMapper.toEntity(request);
    log.info("Creating parking level for parking lot ID: {}", request.getParkingLotId());
    ParkingLevel savedParkingLevel = parkingLevelRepository.save(parkingLevel);
    log.info("Successfully created parking level with ID: {}", savedParkingLevel.getId());
    return parkingLevelMapper.toResponse(savedParkingLevel);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting parking level with id: {}", id);
    if (!parkingLevelRepository.existsById(id)) {
      throw new ResourceNotFoundException("Parking level not found with id: " + id);
    }
    parkingLevelRepository.deleteById(id);
  }
}
