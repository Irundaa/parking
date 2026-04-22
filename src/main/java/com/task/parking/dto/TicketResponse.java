package com.task.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.task.parking.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
  private Long id;
  private String licensePlate;
  private Long parkingSlotId;
  private LocalDateTime entryTime;
  private LocalDateTime exitTime;
  private BigDecimal fee;
  private Status status;
}
