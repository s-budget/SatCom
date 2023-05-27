package hr.fer.progi.satcom.services.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class RequestDeniedException extends RuntimeException {
    public RequestDeniedException(String message) {
        super(message);
    }

}
