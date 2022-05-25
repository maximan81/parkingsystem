package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        final int NUMBERS_OF_TIMES_FOR_DISCOUNT = 2;
        final double DISCOUNT = (double) 5 / 100;

        long durationInSeconds = ticket.getOutTime().getTime() / 1000 - ticket.getInTime().getTime() / 1000;

        float duration = durationInSeconds / 3600F;

        if (duration > 0.50) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    System.out.println(NUMBERS_OF_TIMES_FOR_DISCOUNT);
                    double priceDiscount = (duration * Fare.CAR_RATE_PER_HOUR) - (duration * Fare.CAR_RATE_PER_HOUR * DISCOUNT);
                    double price = duration * Fare.CAR_RATE_PER_HOUR;
                    ticket.setPrice(ticket.getRepeatUsers() >= NUMBERS_OF_TIMES_FOR_DISCOUNT ? priceDiscount : price);
                    break;
                }
                case BIKE: {
                    double priceDiscount = duration * Fare.BIKE_RATE_PER_HOUR - duration * Fare.BIKE_RATE_PER_HOUR * DISCOUNT;
                    double price = duration * Fare.BIKE_RATE_PER_HOUR;
                    ticket.setPrice(ticket.getRepeatUsers() > NUMBERS_OF_TIMES_FOR_DISCOUNT ? priceDiscount : price);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }else {
            ticket.setPrice(0.0);
        }

    }
}