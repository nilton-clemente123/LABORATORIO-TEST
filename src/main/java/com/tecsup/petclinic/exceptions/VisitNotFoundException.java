package com.tecsup.petclinic.exceptions;

public class VisitNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public VisitNotFoundException(String message) {
        super(message);
    }
}
