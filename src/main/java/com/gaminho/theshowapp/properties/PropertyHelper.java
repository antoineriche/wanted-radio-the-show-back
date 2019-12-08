package com.gaminho.theshowapp.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PropertyHelper {

    private static Logger log = LoggerFactory.getLogger(PropertyHelper.class);

    private final String propertyFile;
    private final Properties properties;

    PropertyHelper(String propertyFile) {
        this.propertyFile = propertyFile;
        this.properties = new Properties();
        try (InputStream input = PropertyHelper.class.getClassLoader().getResourceAsStream(propertyFile)) {
            properties.load(input);
        } catch (IOException ex) {
            log.error("can not load properties", ex);
        }
    }

    String readProperty(String key){
        return properties.getProperty(key);
    }

    void writeProperty(String key, Object value){
        try (FileOutputStream fr = new FileOutputStream(getPropertyFile())) {
            properties.setProperty(key, String.valueOf(value));
            properties.store(fr, "Update Guest Folder ID");
        } catch (Exception e){
            log.error("can not store properties", e);
        }
    }

    private File getPropertyFile(){
        return new File(PropertyHelper.class.getClassLoader().getResource(this.propertyFile).getFile());
    }
}
