package hr.fer.progi.satcom.services.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class EntityMissingException extends RuntimeException {

    public EntityMissingException(Class<?> cls, Object ref) {
        super("Entity with reference " + ref + " of " + cls + " not found.");
    }

}
