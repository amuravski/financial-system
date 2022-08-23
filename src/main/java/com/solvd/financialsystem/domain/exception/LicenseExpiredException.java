package com.solvd.financialsystem.domain.exception;

public class LicenseExpiredException extends RuntimeException {

    public LicenseExpiredException(String errorMessage) {
        super(errorMessage);
    }

}
