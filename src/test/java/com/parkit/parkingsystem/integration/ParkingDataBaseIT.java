package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDao;
import com.parkit.parkingsystem.dao.TicketDao;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

  private static final String VEHICLE_REG_NUMBER = "ABCDEF";
  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
  private static ParkingSpotDao parkingSpotDAO;
  private static TicketDao ticketDAO;
  private static DataBasePrepareService dataBasePrepareService;
  @Mock
  private static InputReaderUtil inputReaderUtil;

  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDAO = new ParkingSpotDao();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    ticketDAO = new TicketDao();
    ticketDAO.dataBaseConfig = dataBaseTestConfig;
    dataBasePrepareService = new DataBasePrepareService();
  }

  @AfterAll
  private static void tearDown() {

  }

  @BeforeEach
  private void setUpPerTest() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(VEHICLE_REG_NUMBER);
    dataBasePrepareService.clearDataBaseEntries();
  }

  @Test
  public void testParkingACar() {

    //GIVEN
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    parkingService.processIncomingVehicle();

    // WHEN
    Ticket ticket = ticketDAO.getTicket(VEHICLE_REG_NUMBER);

    // THEN
    // check that a ticket is actualy saved in DB and Parking table is updated with availability
    assertEquals(VEHICLE_REG_NUMBER, ticket.getVehicleRegNumber());
    assertFalse(ticket.getParkingSpot().isAvailable());

  }

  @Test
  public void testParkingLotExit() {
    // GIVEN
    testParkingACar();
    Ticket inComingTicket = ticketDAO.getTicket(VEHICLE_REG_NUMBER);
    long outTime = inComingTicket.getInTime().getTime() + 45 * 60 * 1000;
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);


    // WHEN
    parkingService.processExitingVehicle(new Date(outTime));
    Ticket ticket = ticketDAO.getTicket(VEHICLE_REG_NUMBER);

    // THEN
    // check that the fare generated and out time are populated correctly in the database
    assertEquals(0.75 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    assertEquals(outTime, ticket.getOutTime().getTime());
  }


  @Test()
  public void testExitingCarDiscountWithTwoIncomingTime() {
    //GIVEN
    final double DISCOUNT = (double) 5 / 100;
    testParkingACar();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    parkingService.processExitingVehicle();
    testParkingACar();
    Ticket inComingTicket = ticketDAO.getTicket(VEHICLE_REG_NUMBER);
    long outTime = inComingTicket.getInTime().getTime() + 45 * 60 * 1000;
    double priceDiscount =
        (0.75 * Fare.CAR_RATE_PER_HOUR) - (0.75 * Fare.CAR_RATE_PER_HOUR * DISCOUNT);

    // WHEN
    parkingService.processExitingVehicle(new Date(outTime));
    Ticket ticket = ticketDAO.getTicket(VEHICLE_REG_NUMBER);

    // THEN
    assertEquals(priceDiscount, ticket.getPrice());

  }

}
