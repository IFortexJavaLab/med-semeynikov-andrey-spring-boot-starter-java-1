package com.ifortex.internship.medstarter.exception;

public class MedServiceException extends RuntimeException {
    public MedServiceException(String message) {
        super(message);
    }

    public MedServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
