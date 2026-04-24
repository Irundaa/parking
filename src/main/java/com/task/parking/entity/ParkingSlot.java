package com.task.parking.entity;

import com.task.parking.enums.ParkingSlotType;
import com.task.parking.enums.SlotStatus;
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
@Table(name = "parking_slot")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ParkingSlotType type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SlotStatus status;

  @ManyToOne
  @JoinColumn(name = "parking_level_id", referencedColumnName = "id", nullable = false)
  private ParkingLevel parkingLevel;
}
