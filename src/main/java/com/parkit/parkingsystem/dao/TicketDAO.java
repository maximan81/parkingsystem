package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DbConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The TicketDao class implements the logic
 * of data access for a ticket object.
 *
 */
public class TicketDao {

  private static final Logger logger = LogManager.getLogger("TicketDao");

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * saveTicket. method that save a ticket in
   * the database
   *
   * @param ticket the ticket object
   */
  public void saveTicket(Ticket ticket) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DbConstants.SAVE_TICKET);

      ps.setInt(1, ticket.getParkingSpot().getId());
      ps.setString(2, ticket.getVehicleRegNumber());
      ps.setDouble(3, ticket.getPrice());
      ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
      ps.setTimestamp(5,
          (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));

      ps.execute();
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
  }

  /**
   * saveTicket. method that get a ticket from
   * the database
   *
   * @param vehicleRegNumber the vehicle registration number
   */
  public Ticket getTicket(String vehicleRegNumber) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Ticket ticket = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DbConstants.GET_TICKET);
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      while (rs.next()) {
        ticket = new Ticket();
        ticket.setRepeatUsers(rs.getInt(1));
        ParkingSpot parkingSpot =
            new ParkingSpot(rs.getInt(2), ParkingType.valueOf(rs.getString(7)), false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(rs.getInt(3));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(rs.getDouble(4));
        ticket.setInTime(rs.getTimestamp(5));
        ticket.setOutTime(rs.getTimestamp(6));
      }

    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return ticket;
  }

  /**
   * updateTicket. method that update a ticket in
   * the database
   *
   * @param ticket a ticket object
   */
  public boolean updateTicket(Ticket ticket) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DbConstants.UPDATE_TICKET);
      ps.setDouble(1, ticket.getPrice());
      ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
      ps.setInt(3, ticket.getId());
      ps.execute();
      return true;
    } catch (Exception ex) {
      logger.error("Error saving ticket info", ex);
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }
}
