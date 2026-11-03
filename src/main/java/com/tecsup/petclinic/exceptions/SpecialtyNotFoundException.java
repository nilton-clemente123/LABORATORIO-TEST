package com.tecsup.petclinic.exceptions;

public class SpecialtyNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public SpecialtyNotFoundException(String message) {
        super(message);
    }
}
