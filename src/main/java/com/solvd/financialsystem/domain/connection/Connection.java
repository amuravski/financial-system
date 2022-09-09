package com.solvd.financialsystem.domain.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);

    public void create() {
        sleep(1000);
        LOGGER.info("Created");
    }

    public void read() {
        sleep(1000);
        LOGGER.info("Read");
    }

    public void update() {
        sleep(1000);
        LOGGER.info("Updated");
    }

    public void delete() {
        sleep(1000);
        LOGGER.info("Deleted");
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
