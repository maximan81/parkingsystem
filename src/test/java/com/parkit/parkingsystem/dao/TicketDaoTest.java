package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Date;

@Tag("TicketDaoTest")
@DisplayName("Ticket data access object")
class TicketDaoTest {

  private static final String REGISTRATION_NUMBER = "ABCDEF";

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
  @Test
  @DisplayName("Save ticket in database")
  public void saveTicket() {
    // GIVEN
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Ticket ticket = new Ticket();
    ticket.setId(1);
    ticket.setPrice(0.0);
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setOutTime(null);
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber(REGISTRATION_NUMBER);

    // WHEN
    ticketDao.saveTicket(ticket);

    // THEN
    assertThat(ticketDao.getTicket(REGISTRATION_NUMBER)
        .getId())
        .isEqualTo(1);
    assertThat(ticketDao.getTicket(REGISTRATION_NUMBER)
        .getVehicleRegNumber())
        .isEqualTo(REGISTRATION_NUMBER);
  }

  @Test
  @DisplayName("Get ticket from database")
  public void getTicket() {
    // GIVEN
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Ticket ticket = new Ticket();
    ticket.setId(1);
    ticket.setPrice(0.0);
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setOutTime(null);
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
    ticketDao.saveTicket(ticket);
    // WHEN

    Ticket dbTicket = ticketDao.getTicket(REGISTRATION_NUMBER);

    // THEN
    assertThat(dbTicket
        .getId())
        .isEqualTo(1);
    assertThat(dbTicket
        .getVehicleRegNumber())
        .isEqualTo(REGISTRATION_NUMBER);
  }

  @Test
  @DisplayName("update a ticket in database")
  public void updateTicket() {
    // GIVEN
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Ticket ticket = new Ticket();
    ticket.setId(1);
    ticket.setPrice(0.0);
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setOutTime(null);
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
    ticketDao.saveTicket(ticket);

    Ticket updateTicket = new Ticket();
    updateTicket.setPrice(1.6);
    updateTicket.setId(1);
    updateTicket.setOutTime(new Date(System.currentTimeMillis() - (120 * 60 * 1000)));

    // WHEN
    boolean updated = ticketDao.updateTicket(updateTicket);

    // THEN
    assertTrue(updated);
  }
}