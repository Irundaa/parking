package com.task.parking.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.parking.dto.ParkingLotRequest;
import com.task.parking.entity.ParkingLot;
import com.task.parking.repository.ParkingLotRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ParkingLotIntegrationTest {

  private static final String BASE_URL = "/api/v1/lots";
  private static final String LOT_NAME = "Integration Test Parking Lot";
  private static final Long NON_EXISTENT_ID = 999L;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ParkingLotRepository parkingLotRepository;

  @Test
  void createLotShouldSaveToDatabaseAndReturnCreated() throws Exception {
    ParkingLotRequest request = buildRequest(LOT_NAME);

    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(LOT_NAME))
        .andExpect(jsonPath("$.id").isNumber());

    List<ParkingLot> lots = parkingLotRepository.findAll();
    assertThat(lots).hasSize(1);
    assertThat(lots.get(0).getName()).isEqualTo(LOT_NAME);
  }

  @Test
  void createLotShouldReturnBadRequestWhenNameIsBlank() throws Exception {
    ParkingLotRequest request = buildRequest("");

    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteLotShouldRemoveFromDatabaseAndReturnNoContent() throws Exception {
    ParkingLot parkingLot = buildEntity(LOT_NAME);
    ParkingLot savedLot = parkingLotRepository.save(parkingLot);

    mockMvc.perform(delete(BASE_URL + "/{id}", savedLot.getId()))
        .andExpect(status().isNoContent());

    assertThat(parkingLotRepository.findById(savedLot.getId())).isEmpty();
  }

  @Test
  void deleteLotShouldReturnNotFoundWhenLotDoesNotExist() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/{id}", NON_EXISTENT_ID))
        .andExpect(status().isNotFound());
  }

  private ParkingLotRequest buildRequest(String name) {
    ParkingLotRequest request = new ParkingLotRequest();
    request.setName(name);
    return request;
  }

  private ParkingLot buildEntity(String name) {
    ParkingLot entity = new ParkingLot();
    entity.setName(name);
    return entity;
  }
}
