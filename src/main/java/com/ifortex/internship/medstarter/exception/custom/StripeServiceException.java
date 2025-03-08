package com.ifortex.internship.medstarter.exception.custom;

import com.ifortex.internship.medstarter.exception.MedServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StripeServiceException extends MedServiceException {
    public StripeServiceException(String message) {
        super(message);
    }
}
