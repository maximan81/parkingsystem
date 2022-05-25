package com.parkit.parkingsystem.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Credentials {

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
