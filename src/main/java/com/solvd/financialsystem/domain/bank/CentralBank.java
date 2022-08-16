package com.solvd.financialsystem.domain.bank;

import com.solvd.financialsystem.domain.Meetable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CentralBank implements Meetable {

    private BigDecimal keyRate;
    private BigDecimal inflationTarget;
    private LocalDateTime nextMeetingDate;
    private BigDecimal currentInflation;

    public CentralBank(BigDecimal keyRate, BigDecimal inflationTarget, LocalDateTime nextMeetingDate) {
        this.keyRate = keyRate;
        this.inflationTarget = inflationTarget;
        this.nextMeetingDate = nextMeetingDate;
    }

    public BigDecimal getKeyRate() {
        return keyRate;
    }

    public BigDecimal getInflationTarget() {
        return inflationTarget;
    }

    public void setInflationTarget(BigDecimal inflationTarget) {
        this.inflationTarget = inflationTarget;
    }

    public void setKeyRate(BigDecimal keyRate) {
        this.keyRate = keyRate;
    }

    public BigDecimal getCurrentInflation() {
        return currentInflation;
    }

    public void setCurrentInflation(BigDecimal currentInflation) {
        this.currentInflation = currentInflation;
    }

    public LocalDateTime getNextMeetingDate() {
        return nextMeetingDate;
    }

    public void setNextMeetingDate(LocalDateTime nextMeetingDate) {
        this.nextMeetingDate = nextMeetingDate;
    }

    @Override
    public void meet() {
        if (LocalDateTime.now().compareTo(nextMeetingDate) > 0) {
            if (currentInflation.compareTo(inflationTarget) > 0) {
                keyRate = keyRate.add(new BigDecimal("0.5"));
            } else if (currentInflation.compareTo(inflationTarget) < 0) {
                keyRate = keyRate.subtract(new BigDecimal("0.5"));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CentralBank)) return false;
        CentralBank other = (CentralBank) o;
        return keyRate.equals(other.keyRate) &&
                Objects.equals(inflationTarget, other.inflationTarget) &&
                Objects.equals(nextMeetingDate, other.nextMeetingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyRate, inflationTarget, nextMeetingDate);
    }

    @Override
    public String toString() {
        return "CentralBank{" +
                "keyRate=" + keyRate +
                ", inflationTarget=" + inflationTarget +
                ", nextMeetingDate=" + nextMeetingDate +
                '}';
    }
}
