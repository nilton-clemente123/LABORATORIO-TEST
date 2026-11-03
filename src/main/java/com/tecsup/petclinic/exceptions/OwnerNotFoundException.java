package com.tecsup.petclinic.exceptions;

public class OwnerNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public OwnerNotFoundException(String message) {
        super(message);
    }
}
