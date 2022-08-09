package domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.UnexpectedException;

public class Conference implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(Conference.class);

    public void holdConference(int members) throws UnexpectedException {
        if (members < 1) {
            throw new UnexpectedException("No conference members.");
        }
        LOGGER.info("The conference was held.");
    }

    @Override
    public void close() {
    }

    public static void closeConference(){
        LOGGER.info("ty lads");
    }
}
