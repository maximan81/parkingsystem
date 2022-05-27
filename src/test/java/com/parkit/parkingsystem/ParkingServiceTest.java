package com.parkit.parkingsystem;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDao;
import com.parkit.parkingsystem.dao.TicketDao;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
@Tag("ParkingServiceTest")
@DisplayName("Parking service test")
public class ParkingServiceTest {
  private static String REGISTRATION_NUMBER = "ABCDEF";

  private static ParkingService parkingService;

  @Mock
  private static InputReaderUtil inputReaderUtil;
  @Mock
  private static ParkingSpotDao parkingSpotDao;
  @Mock
  private static TicketDao ticketDao;

  @BeforeEach
  private void setUpPerTest() {

  }

  @Test
  @DisplayName("Succeed process incoming car vehicle")
  public void processIncomingCarVehicleTest() {

    try {
      // GIVEN
      when(inputReaderUtil.readSelection()).thenReturn(1);
      when(parkingSpotDao.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REGISTRATION_NUMBER);
      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      Ticket ticket = new Ticket();
      ticket.setId(1);
      ticket.setPrice(0.0);
      ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
      ticket.setOutTime(null);
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
      parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);
      ticketDao.saveTicket(ticket);

      // WHEN
      parkingService.processIncomingVehicle();

      // THEN
      verify(ticketDao, Mockito.times(1)).saveTicket(ticket);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock objects");
    }}

  @Test
  @DisplayName("Succeed process incoming bike vehicle")
  public void processIncomingBikeVehicleTest() {

    try {
      // GIVEN
      when(inputReaderUtil.readSelection()).thenReturn(2);
      when(parkingSpotDao.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(2);
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REGISTRATION_NUMBER);
      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
      Ticket ticket = new Ticket();
      ticket.setId(1);
      ticket.setPrice(0.0);
      ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
      ticket.setOutTime(null);
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
      parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);
      ticketDao.saveTicket(ticket);

      // WHEN
      parkingService.processIncomingVehicle();

      // THEN
      verify(ticketDao, Mockito.times(1)).saveTicket(ticket);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock objects");
    }}

  @Test
  @DisplayName("Fail get type vehicle")
  public void failGetTypeVehicle() throws Exception {

    // GIVEN
    when(inputReaderUtil.readSelection()).thenReturn(0);

    // WHEN
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);

    // THEN
    assertThrows(IllegalArgumentException.class, () -> parkingService.getVehicleType());
  }

  @Test
  @DisplayName("Fail process incoming vehicle")
  public void failProcessIncomingVehicleTest() throws Exception {

      // WHEN
      when(inputReaderUtil.readSelection()).thenReturn(1);
      when(parkingSpotDao.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(Exception.class);
      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      Ticket ticket = new Ticket();
      ticket.setId(1);
      ticket.setPrice(0.0);
      ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
      ticket.setOutTime(null);
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
      parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);
      ticketDao.saveTicket(ticket);

      // WHEN
      parkingService.processIncomingVehicle();

      // THEN
      assertThrows(Exception.class, () -> {
        inputReaderUtil.readVehicleRegistrationNumber();
      });

  }
  @Test
  @DisplayName("Succeed process exiting vehicle")
  public void processExitingVehicleTest() {
    try {
      // GIVEN
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REGISTRATION_NUMBER);
      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      Ticket ticket = new Ticket();
      ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
      when(ticketDao.getTicket(anyString())).thenReturn(ticket);
      when(ticketDao.updateTicket(any(Ticket.class))).thenReturn(true);
      when(parkingSpotDao.updateParking(any(ParkingSpot.class))).thenReturn(true);
      parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);

      // WHEN
      parkingService.processExitingVehicle();

      // THEN
      verify(parkingSpotDao, Mockito.times(1)).updateParking(any(ParkingSpot.class));

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock objects");
    }
 }

  @Test
  @DisplayName("Fail process exiting vehicle")
  public void failProcessExitingVehicleTest() {
    try {
      // GIVEN
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(Exception.class);
      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      Ticket ticket = new Ticket();
      ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
      parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);

      // WHEN
      parkingService.processExitingVehicle();

      // THEN
      assertThrows(Exception.class, () -> {
        inputReaderUtil.readVehicleRegistrationNumber();
      });

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock objects");
    }
  }

  @Test
  @DisplayName("Fail get next parking number if available")
  public void failGetNextParkingNumberIsLessThanZero() {

    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDao.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Ticket ticket = new Ticket();
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber(REGISTRATION_NUMBER);
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);
    ticketDao.saveTicket(ticket);

    try {
      // WHEN
      parkingService.getNextParkingNumberIfAvailable();

    } catch (Exception exception) {
      // THEN
      assertTrue(exception.getMessage()
          .contains("Error fetching parking number from DB. Parking slots might be full"));
    }

  }


}
