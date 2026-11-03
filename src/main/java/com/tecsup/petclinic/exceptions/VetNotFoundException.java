package com.tecsup.petclinic.exceptions;

public class VetNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public VetNotFoundException(String message) {
        super(message);
    }
}
