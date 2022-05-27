package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * The FareCalculatorService class implements the business logic
 * of parking fare calculation.
 *
 */
public class FareCalculatorService {
  /**
   * calculateFare. method that calculate a price of
   * parking fare
   *
   * @param ticket the ticket object
   */
  public void calculateFare(Ticket ticket) {
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    final int NumberOfTimesForDiscount = 2;
    final double Discount = (double) 5 / 100;

    long durationInSeconds =
        ticket.getOutTime().getTime() / 1000 - ticket.getInTime().getTime() / 1000;

    float duration = durationInSeconds / 3600F;

    if (duration > 0.50) {
      switch (ticket.getParkingSpot().getParkingType()) {
        case CAR: {
          double priceDiscount =
              (duration * Fare.CAR_RATE_PER_HOUR) - (duration * Fare.CAR_RATE_PER_HOUR * Discount);
          double price = duration * Fare.CAR_RATE_PER_HOUR;
          ticket.setPrice(
              ticket.getRepeatUsers() >= NumberOfTimesForDiscount ? priceDiscount : price);
          break;
        }
        case BIKE: {
          double priceDiscount =
              duration * Fare.BIKE_RATE_PER_HOUR - duration * Fare.BIKE_RATE_PER_HOUR * Discount;
          double price = duration * Fare.BIKE_RATE_PER_HOUR;
          ticket.setPrice(
              ticket.getRepeatUsers() > NumberOfTimesForDiscount ? priceDiscount : price);
          break;
        }
        default:
          throw new IllegalArgumentException("Unkown Parking Type");
      }
    } else {
      ticket.setPrice(0.0);
    }

  }
}