package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * The ParkingSpot class implements parking spot
 * entity.
 *
 */
public class ParkingSpot {
  private int number;
  private ParkingType parkingType;
  private boolean isAvailable;

  /**
   * constructor for class ParkingSpot. method
   * that instantiates an objet
   *
   * @param number  an id of parking spot
   * @param parkingType  a parking spot type
   * @param isAvailable  check availability of parking spot
   */
  public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
    this.number = number;
    this.parkingType = parkingType;
    this.isAvailable = isAvailable;
  }

  public int getId() {
    return number;
  }

  public void setId(int number) {
    this.number = number;
  }

  public ParkingType getParkingType() {
    return parkingType;
  }

  public void setParkingType(ParkingType parkingType) {
    this.parkingType = parkingType;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParkingSpot that = (ParkingSpot) o;
    return number == that.number;
  }

  @Override
  public int hashCode() {
    return number;
  }
}
