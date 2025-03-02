
package com.orleansgo.administrateur.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdministrateurNotFoundException extends RuntimeException {
    public AdministrateurNotFoundException(String message) {
        super(message);
    }
}
