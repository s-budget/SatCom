package hr.fer.progi.satcom.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.satcom.security.payload.response.ErrorResponse;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestControllerAdvice
public class CustomErrorHandler implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public void errorHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        final Map<String, Object> body = new HashMap<>();
        body.put("error", "Requested resource could not be found");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    @ExceptionHandler(RequestDeniedException.class)
    public void handleCustomException(RequestDeniedException e, HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        final Map<String, Object> body = new HashMap<>();
        body.put("error", new ErrorResponse(e.getMessage()).getError());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleCustomException(IllegalArgumentException e, HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        final Map<String, Object> body = new HashMap<>();
        body.put("error", new ErrorResponse(e.getMessage()).getError());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    @ExceptionHandler(EntityMissingException.class)
    public void handleCustomException(EntityMissingException e, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        final Map<String, Object> body = new HashMap<>();
        body.put("error", new ErrorResponse(e.getMessage()).getError());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
