package com.task.parking.service.impl;

import java.util.List;
import com.task.parking.dto.ParkingSlotRequest;
import com.task.parking.dto.ParkingSlotResponse;
import com.task.parking.entity.ParkingSlot;
import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
import com.task.parking.mapper.ParkingSlotMapper;
import com.task.parking.repository.ParkingSlotRepository;
import com.task.parking.service.ParkingSlotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingSlotServiceImpl implements ParkingSlotService {

  private final ParkingSlotRepository parkingSlotRepository;
  private final ParkingSlotMapper parkingSlotMapper;

  @Override
  @Transactional
  public ParkingSlotResponse create(ParkingSlotRequest request) {
    ParkingSlot parkingSlot = parkingSlotMapper.toEntity(request);
    parkingSlot.setStatus(SlotStatus.AVAILABLE);
    ParkingSlot savedParkingSlot = parkingSlotRepository.save(parkingSlot);
    log.info("Successfully created parking slot with ID: {}", savedParkingSlot.getId());
    return parkingSlotMapper.toResponse(savedParkingSlot);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ParkingSlotResponse> getAvailableSlots() {
    log.info("Fetching all available parking slots");
    return parkingSlotRepository.findAllByStatus(SlotStatus.AVAILABLE)
        .stream()
        .map(parkingSlotMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ParkingSlotResponse> getOccupiedSlots() {
    log.info("Fetching all occupied parking slots");
    return parkingSlotRepository.findAllByStatus(SlotStatus.OCCUPIED)
        .stream()
        .map(parkingSlotMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ParkingSlot getAvailableSlot(List<ParkingSlotType> allowedTypes){
    log.info("Fetching available parking slot");
    return parkingSlotRepository.findFirstByStatusAndTypeIn(SlotStatus.AVAILABLE, allowedTypes)
        .orElseThrow(() -> new EntityNotFoundException("Parking slot not found for this type"));
  }

  @Override
  @Transactional
  public void changeStatus(Long id, SlotStatus status) {
    log.info("Updating status of parking slot ID: {} to {}", id, status);

    ParkingSlot slot = parkingSlotRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Parking slot not found with ID: " + id));

    slot.setStatus(status);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting parking slot with ID: {}", id);
    if (!parkingSlotRepository.existsById(id)) {
      throw new EntityNotFoundException("Parking slot not found with ID: " + id);
    }
    parkingSlotRepository.deleteById(id);
  }
}
