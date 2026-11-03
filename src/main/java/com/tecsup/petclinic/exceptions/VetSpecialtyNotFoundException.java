package com.tecsup.petclinic.exceptions;

public class VetSpecialtyNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public VetSpecialtyNotFoundException(String message) {
        super(message);
    }
}
