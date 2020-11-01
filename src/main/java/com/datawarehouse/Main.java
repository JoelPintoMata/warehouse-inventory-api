package com.datawarehouse;

import com.datawarehouse.loader.DataLoaderManager;
import com.datawarehouse.loader.exception.DataLoaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                SpringApplication.run(Main.class, args);
        // load the data
        try {
            applicationContext.getBean(DataLoaderManager.class).load();
        } catch (DataLoaderException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}