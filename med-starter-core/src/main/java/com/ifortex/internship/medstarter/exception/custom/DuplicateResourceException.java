package com.ifortex.internship.medstarter.exception.custom;

import com.ifortex.internship.medstarter.exception.MedServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends MedServiceException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
