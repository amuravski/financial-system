package domain.company;

import domain.exception.IllegalAmountOfMembersException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.math.BigDecimal;
import java.util.Objects;

public class LLC extends AbstractCompany {

    private static final Logger logger = (Logger) LogManager.getRootLogger();
    private static final int MIN_LLC_SHAREHOLDERS_AMOUNT = 2;
    private static final int MAX_LLC_SHAREHOLDERS_AMOUNT = 50;

    private int shareHoldersAmount;

    public LLC(String name, BigDecimal assets, BigDecimal liabilities, int shareHoldersAmount) throws IllegalAmountOfMembersException {
        super(name, assets, liabilities);
        setShareHoldersAmount(shareHoldersAmount);
    }

    public LLC(String name, int shareHoldersAmount) throws IllegalAmountOfMembersException {
        super(name);
        setShareHoldersAmount(shareHoldersAmount);
    }

    public LLC(String name) {
        super(name);
    }

    public int getShareHoldersAmount() {
        return shareHoldersAmount;
    }

    public void setShareHoldersAmount(int shareHoldersAmount) throws IllegalAmountOfMembersException {
        if (shareHoldersAmount >= MIN_LLC_SHAREHOLDERS_AMOUNT && shareHoldersAmount <= MAX_LLC_SHAREHOLDERS_AMOUNT) {
            this.shareHoldersAmount = shareHoldersAmount;
        } else {
            throw new IllegalAmountOfMembersException("Shareholders number of " + shareHoldersAmount + " not supported.");
        }
    }

    @Override
    public void meet() {
        logger.info("LLC shareholders meeting with a size of " + shareHoldersAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LLC)) return false;
        if (!super.equals(o)) return false;
        LLC other = (LLC) o;
        return shareHoldersAmount == other.shareHoldersAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shareHoldersAmount);
    }

    @Override
    public String toString() {
        return "LLC{" +
                "shareHoldersAmount=" + shareHoldersAmount + ", " + super.toString() +
                '}';
    }
}
