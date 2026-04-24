package com.task.parking.service.impl;

import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.dto.ParkingLotResponse;
import com.task.parking.entity.ParkingLot;
import com.task.parking.mapper.ParkingLotMapper;
import com.task.parking.repository.ParkingLotRepository;
import com.task.parking.service.ParkingLotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingLotServiceImpl implements ParkingLotService {

  private final ParkingLotRepository parkingLotRepository;
  private final ParkingLotMapper parkingLotMapper;

  @Override
  @Transactional
  public ParkingLotResponse create(ParkingLotRequest request) {
    ParkingLot parkingLot = parkingLotMapper.toEntity(request);
    log.info("Creating parking lot: {}", parkingLot.getName());
    ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);
    return parkingLotMapper.toResponse(savedParkingLot);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting parking lot: {}", id);
    if (!parkingLotRepository.existsById(id)) {
      throw new EntityNotFoundException("Parking lot not found with id: " + id);
    }
    parkingLotRepository.deleteById(id);
  }
}
