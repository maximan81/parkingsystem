package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDao;
import com.parkit.parkingsystem.dao.TicketDao;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ParkingService class implements the
 * parking service logic.
 */
public class ParkingService {

  private static final Logger logger = LogManager.getLogger("ParkingService");

  private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

  private InputReaderUtil inputReaderUtil;
  private ParkingSpotDao parkingSpotDao;
  private TicketDao ticketDao;

  /**
   * constructor for class ParkingService. method
   * that instantiates an objet
   *
   * @param inputReaderUtil  an input reader object
   * @param parkingSpotDao  a parking spot data access model object
   * @param ticketDao  a ticket data access model object
   */
  public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDao parkingSpotDao,
                        TicketDao ticketDao) {
    this.inputReaderUtil = inputReaderUtil;
    this.parkingSpotDao = parkingSpotDao;
    this.ticketDao = ticketDao;
  }

  /**
   * processIncomingVehicle. method that process
   * incoming vehicle
   *
   */
  public void processIncomingVehicle() {
    try {
      ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
      if (parkingSpot != null && parkingSpot.getId() > 0) {
        final String vehicleRegNumber = getVehicleRegNumber();
        parkingSpot.setAvailable(false);
        parkingSpotDao.updateParking(
            parkingSpot);

        Date inTime = new Date();
        Ticket ticket = new Ticket();

        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticketDao.saveTicket(ticket);
        System.out.println("Generated Ticket and saved in DB");
        System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
        System.out.println(
            "Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
      }
    } catch (Exception e) {
      logger.error("Unable to process incoming vehicle", e);
    }
  }

  /**
   * getVehicleRegNumber. method that get vehicle
   * registration number
   *
   */
  private String getVehicleRegNumber() throws Exception {
    System.out.println("Please type the vehicle registration number and press enter key");
    return inputReaderUtil.readVehicleRegistrationNumber();
  }

  /**
   * getNextParkingNumberIfAvailable. method that get
   * next parking number if it's available
   *
   */
  public ParkingSpot getNextParkingNumberIfAvailable() {
    int parkingNumber = 0;
    ParkingSpot parkingSpot = null;
    try {
      ParkingType parkingType = getVehicleType();
      parkingNumber = parkingSpotDao.getNextAvailableSlot(parkingType);
      if (parkingNumber > 0) {
        parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
      } else {
        throw new Exception("Error fetching parking number from DB. Parking slots might be full");
      }
    } catch (IllegalArgumentException ie) {
      logger.error("Error parsing user input for type of vehicle", ie);
      throw new IllegalArgumentException("Error parsing user input for type of vehicle");
    } catch (Exception e) {
      logger.error("Error fetching next available parking slot", e);
    }
    return parkingSpot;
  }

  /**
   * getVehicleType. method that get vehicle type
   *
   */
  public ParkingType getVehicleType() {
    System.out.println("Please select vehicle type from menu");
    System.out.println("1 CAR");
    System.out.println("2 BIKE");
    int input = inputReaderUtil.readSelection();
    switch (input) {
      case 1: {
        return ParkingType.CAR;
      }
      case 2: {
        return ParkingType.BIKE;
      }
      default: {
        System.out.println("Incorrect input provided");
        throw new IllegalArgumentException("Entered input is invalid");
      }
    }
  }

  /**
   * processExitingVehicle. method process existing
   * vehicle
   *
   * @param dates an elipse of Date just for test
   */
  public void processExitingVehicle(Date... dates) {
    try {
      String vehicleRegNumber = getVehicleRegNumber();
      System.out.println("before");
      Ticket ticket = ticketDao.getTicket(vehicleRegNumber);
      Date outTime = dates.length != 0 ? dates[0] : new Date();
      ticket.setOutTime(outTime);

      fareCalculatorService.calculateFare(ticket);
      if (ticketDao.updateTicket(ticket)) {
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpot.setAvailable(true);
        parkingSpotDao.updateParking(parkingSpot);
        System.out.println("Please pay the parking fare:" + ticket.getPrice());
        System.out.println(
            "Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:"
                +
                outTime);
      } else {
        System.out.println("Unable to update ticket information. Error occurred");
      }
    } catch (Exception e) {
      logger.error("Unable to process exiting vehicle", e);
    }
  }
}
