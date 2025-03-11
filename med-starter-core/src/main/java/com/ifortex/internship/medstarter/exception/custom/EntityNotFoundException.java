package com.ifortex.internship.medstarter.exception.custom;

import com.ifortex.internship.medstarter.exception.MedServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotFoundException extends MedServiceException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
