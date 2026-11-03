package com.tecsup.petclinic.exceptions;

/**
 * Base class for "record not found" exceptions thrown by the service layer.
 */
public class NotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
