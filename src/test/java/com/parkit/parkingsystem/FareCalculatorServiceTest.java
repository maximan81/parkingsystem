package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Date;

@Tag("FareCalculationServiceTest")
@DisplayName("Fare calculation service")
public class FareCalculatorServiceTest {

  private static FareCalculatorService fareCalculatorService;
  private Ticket ticket;

  @BeforeAll
  private static void setUp() {
    fareCalculatorService = new FareCalculatorService();
  }

  @BeforeEach
  private void setUpPerTest() {
    ticket = new Ticket();
  }

  @Test
  @DisplayName("Calculate fare car")
  public void calculateFareCar() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    // THEN
    assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
  }

  @Test
  @DisplayName("Calculate fare bike")
  public void calculateFareBike() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    // THEN
    assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
  }

  @Test
  @DisplayName("Calculate fare unknown type")
  public void calculateFareUnknownType() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);

    // THEN
    assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  @DisplayName("Calculate fare bike with future in time")
  public void calculateFareBikeWithFutureInTime() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);

    // THEN
    assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  @DisplayName("Calculate fare bike with less one hour parking time")
  public void calculateFareBikeWithLessThanOneHourParkingTime() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() -
        (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    //WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    // THEN
    assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  @DisplayName("Calculate fare car with less one hour parking time")
  public void calculateFareCarWithLessThanOneHourParkingTime() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() -
        (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    // THEN
    assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  @DisplayName("Calculate fare car with more a day parking time")
  public void calculateFareCarWithMoreThanADayParkingTime() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() -
        (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    // THEN
    assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }


  @Test
  @DisplayName("Given free bike with less thirty one minute parking time")
  public void givenFreeFareBikeWithLessThirtyOneMinutesParkingTime() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    // WHEN
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    // THEN
    assertEquals((0.0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  @DisplayName("Fail calculation fare parking time")
  public void failCalculateFareParkingTime() {
    // GIVEN
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (75 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.AVION, false);
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);

    // WHEN
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      fareCalculatorService.calculateFare(ticket);
    });

    // THEN
    assertTrue(exception.getMessage().contains("Unkown Parking Type"));
  }

}
