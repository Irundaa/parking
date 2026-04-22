package com.task.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutResponse {
  private Long ticketId;
  private String licensePlate;
  private LocalDateTime entryTime;
  private LocalDateTime exitTime;
  private BigDecimal fee;
}
