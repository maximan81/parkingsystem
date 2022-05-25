package com.parkit.parkingsystem.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

/**
 * The Credentials class implements the logic
 * for managing database settings.
 */
public class Credentials {

  /**
   * loadProps. method that get the object containing all database
   * properties
   *
   * @param filename the file path
   * @return the database properties object
   */
  public static Properties loadProps(String filename) throws IOException {
    Properties props = new Properties();
    if (filename != null) {
      try (
          InputStream propStream = Files.newInputStream(new File(filename).toPath())) {
        props.load(propStream);
      }
    } else {
      System.out.println("Did not load any properties since the property file is not specified");
    }
    return props;
  }
}
