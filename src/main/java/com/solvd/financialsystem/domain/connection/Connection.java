package com.solvd.financialsystem.domain.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);
    private static final int SLEEP_INTERVAL = 500;

    public void create() {
        doAction("Created");
    }

    public void read() {
        doAction("Read");
    }

    public void update() {
        doAction("Updated");
    }

    public void delete() {
        doAction("Deleted");
    }

    private void doAction(String actionName) {
        synchronized (this) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
            LOGGER.info(actionName);
        }
    }
}
