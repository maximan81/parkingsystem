package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DbConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ParkingSpotDao class implements the logic
 * of data access for a parking spot object.
 *
 */
public class ParkingSpotDao {
  private static final Logger logger = LogManager.getLogger("ParkingSpotDao");

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * getNextAvailableSlot. method that get next available
   * slot
   *
   * @param parkingType the parking type object
   */
  public int getNextAvailableSlot(ParkingType parkingType) {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int result = -1;

    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DbConstants.GET_NEXT_PARKING_SPOT);
      ps.setString(1, parkingType.toString());
      rs = ps.executeQuery();

      if (rs.next()) {
        result = rs.getInt(1);
      }

    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return result;
  }

  /**
   * updateParking. method that update parking
   * spot
   *
   * @param parkingSpot the ticket object
   */
  public boolean updateParking(ParkingSpot parkingSpot) {
    Connection con = null;
    PreparedStatement ps = null;

    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DbConstants.UPDATE_PARKING_SPOT);
      ps.setBoolean(1, parkingSpot.isAvailable());
      ps.setInt(2, parkingSpot.getId());
      int updateRowCount = ps.executeUpdate();
      dataBaseConfig.closePreparedStatement(ps);

      return (updateRowCount == 1);
    } catch (Exception ex) {
      logger.error("Error updating parking info", ex);
      return false;
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
  }

}
