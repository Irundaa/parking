package com.task.parking.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResponse {
  private Long ticketId;
  private String licensePlate;
  private Long parkingSlotId;
  private LocalDateTime entryTime;
}
