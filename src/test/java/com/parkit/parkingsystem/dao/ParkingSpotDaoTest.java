package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@Tag("ParkingSpotDaoTest")
@DisplayName("parking spot data access object")
class ParkingSpotDaoTest {


  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

  private static ParkingSpotDao parkingSpotDao;

  private static TicketDao ticketDao;

  private static DataBasePrepareService dataBasePrepareService;


  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDao = new ParkingSpotDao();
    parkingSpotDao.dataBaseConfig = dataBaseTestConfig;
    ticketDao = new TicketDao();
    ticketDao.dataBaseConfig = dataBaseTestConfig;
    dataBasePrepareService = new DataBasePrepareService();
  }

  @BeforeEach
  private void setUpPerTest() throws Exception {
    dataBasePrepareService.clearDataBaseEntries();
  }

  @Test
  @DisplayName("Get next available spot")
  public void getNextAvailableSlot() {
    // GIVEN
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    // WHEN
    int spot = parkingSpotDao.getNextAvailableSlot(parkingSpot.getParkingType());

    // THEN
    assertThat(spot).isEqualTo(parkingSpot.getId());
  }

  @Test
  @DisplayName("Update parking")
  public void updateParking() {
    // GIVEN
    ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.CAR, true);

    // WHEN
    boolean updated = parkingSpotDao.updateParking(parkingSpot);

    // THEN
    assertTrue(updated);
  }
}