package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * The Ticket class implements a ticket
 * entity.
 *
 */
public class Ticket {
  private int id;
  private ParkingSpot parkingSpot;
  private String vehicleRegNumber;
  private double price;
  private Date inTime;
  private Date outTime;

  private int repeatUsers;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ParkingSpot getParkingSpot() {
    return parkingSpot.copy();
  }

  public void setParkingSpot(ParkingSpot parkingSpot) {
    this.parkingSpot = parkingSpot.copy();
  }

  public String getVehicleRegNumber() {
    return vehicleRegNumber;
  }

  public void setVehicleRegNumber(String vehicleRegNumber) {
    this.vehicleRegNumber = vehicleRegNumber;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Date getInTime() {
    return (Date) inTime.clone();
  }

  public void setInTime(Date inTime) {
    this.inTime = (Date) inTime.clone();
  }

  public Date getOutTime() {
    return (Date) outTime.clone();
  }

  public void setOutTime(Date outTime) {
    this.outTime = (Date) outTime.clone();
  }

  public int getRepeatUsers() {
    return repeatUsers;
  }

  public void setRepeatUsers(int repeatUsers) {
    this.repeatUsers = repeatUsers;
  }
}
