package com.task.parking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.task.parking.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "license_plate", nullable = false)
  private Vehicle vehicle;

  @ManyToOne
  @JoinColumn(name = "parking_slot_id", nullable = false)
  private ParkingSlot parkingSlot;

  @Column(name = "entry_time", nullable = false)
  private LocalDateTime entryTime;

  @Column(name = "exit_time")
  private LocalDateTime exitTime;

  @Column
  private BigDecimal fee;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;
}
