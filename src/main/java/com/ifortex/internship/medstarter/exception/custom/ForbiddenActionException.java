package com.ifortex.internship.medstarter.exception.custom;

import com.ifortex.internship.medstarter.exception.MedServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenActionException extends MedServiceException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
