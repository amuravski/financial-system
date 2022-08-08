package domain.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.math.BigDecimal;
import java.util.Objects;

public class CJSC extends AbstractCompany {

    private static final Logger logger = (Logger) LogManager.getRootLogger();

    private int directorsAmount;

    public CJSC(String name, BigDecimal assets, BigDecimal liabilities, int directorsAmount) {
        super(name, assets, liabilities);
        this.directorsAmount = directorsAmount;
    }

    public CJSC(String name, int directorsAmount) {
        super(name);
        this.directorsAmount = directorsAmount;
    }

    public CJSC(String name) {
        super(name);
    }

    public int getDirectorsAmount() {
        return directorsAmount;
    }

    public void setDirectorsAmount(int directorsAmount) {
        this.directorsAmount = directorsAmount;
    }

    @Override
    public void meet() {
        logger.info("CJSC board of directors meeting with a board size of " + directorsAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CJSC)) return false;
        if (!super.equals(o)) return false;
        CJSC other = (CJSC) o;
        return directorsAmount == other.directorsAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), directorsAmount);
    }

    @Override
    public String toString() {
        return "CJSC{" +
                "directorsAmount=" + directorsAmount + ", " + super.toString() +
                '}';
    }
}
