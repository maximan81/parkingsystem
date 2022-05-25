package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.util.Credentials;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The DataBaseConfig class implements the logic
 * for managing database configuration.
 */
public class DataBaseConfig {

  private static final Logger logger = LogManager.getLogger("DataBaseConfig");


  /**
   * getConnection. method that get an database connection instance
   *
   * @return the connection instance
   */
  public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
    Properties properties = Credentials.loadProps("src/main/resources/credentials.properties");

    String url = properties.getProperty("url");
    String user = properties.getProperty("username");
    String pass = properties.getProperty("password");
    String driver = properties.getProperty("driver");

    logger.info("Create DB connection");
    Class.forName(driver);

    return DriverManager.getConnection(url, user, pass);
  }

  /**
   * closeConnection. method that close an database connection instance
   *
   * @param con a database connection instance
   */
  public void closeConnection(Connection con) {
    if (con != null) {
      try {
        con.close();
        logger.info("Closing DB connection");
      } catch (SQLException e) {
        logger.error("Error while closing connection", e);
      }
    }
  }

  /**
   * closeConnection. method that close an database prepare statement
   *
   * @param ps a database prepare statement instance
   */
  public void closePreparedStatement(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        logger.info("Closing Prepared Statement");
      } catch (SQLException e) {
        logger.error("Error while closing prepared statement", e);
      }
    }
  }

  /**
   * closeResultSet. method that close an database result set
   *
   * @param rs a database result set instance
   */
  public void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
        logger.info("Closing Result Set");
      } catch (SQLException e) {
        logger.error("Error while closing result set", e);
      }
    }
  }

}
