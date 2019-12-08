package com.gaminho.theshowapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientPropertyReader {

    private static Logger log = LoggerFactory.getLogger(ClientPropertyReader.class);

    public static String clientOrigin(){
        String clientOrigin = "*";
        try (InputStream input = new FileInputStream("client.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            clientOrigin = String.format("%s:%s",
                    prop.getProperty("client.hostname"),
                    prop.getProperty("client.port"));
        } catch (IOException ex) {
            log.error("can not read client properties", ex);
        }
        return clientOrigin;

    }
}
