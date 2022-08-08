package domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.rmi.UnexpectedException;

public class Conference implements AutoCloseable {

    private static final Logger logger = (Logger) LogManager.getRootLogger();

    public void holdConference(int members) throws UnexpectedException {
        if (members < 1) {
            throw new UnexpectedException("No conference members.");
        }
        logger.info("The conference was held.");
    }

    @Override
    public void close() {
    }
}
