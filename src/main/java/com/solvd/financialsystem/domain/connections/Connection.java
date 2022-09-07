package com.solvd.financialsystem.domain.connections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);

    public void create() {
        LOGGER.info("Created");
    }

    public void read() {
        LOGGER.info("Read");
    }

    public void update() {
        LOGGER.info("Updated");
    }

    public void delete() {
        LOGGER.info("Deleted");
    }
}
