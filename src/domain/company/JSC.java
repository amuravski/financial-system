package domain.company;

import domain.Reportable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Objects;

public class JSC extends AbstractCompany implements Reportable {

    private static final Logger LOGGER = LogManager.getLogger(JSC.class);

    private int publicShares;

    public JSC(String name, BigDecimal assets, BigDecimal liabilities, int publicShares) {
        super(name, assets, liabilities);
        this.publicShares = publicShares;
    }

    public JSC(String name, int publicShares) {
        super(name);
        this.publicShares = publicShares;
    }

    public JSC(String name) {
        super(name);
    }

    public int getPublicShares() {
        return publicShares;
    }

    public void setPublicShares(int publicShares) {
        this.publicShares = publicShares;
    }

    @Override
    public void meet() {
        System.out.println("JSC major shareholders meeting");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSC)) return false;
        if (!super.equals(o)) return false;
        JSC other = (JSC) o;
        return publicShares == other.publicShares;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), publicShares);
    }

    @Override
    public String toString() {
        return "JSC{" +
                "publicShares=" + publicShares + ", " + super.toString() +
                '}';
    }

    @Override
    public void publishReport() {
        LOGGER.info(this.toString() + "\nReported balance: " + getAssets().subtract(getLiabilities()));
    }
}
