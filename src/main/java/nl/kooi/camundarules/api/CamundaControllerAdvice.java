package nl.kooi.camundarules.api;

import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.api.dto.ErrorDto;
import nl.kooi.camundarules.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CamundaControllerAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleUnauthorizedException(UnauthorizedException exception) {
        log.debug(exception.getMessage());
        return new ErrorDto("Unauthorized request");
    }

}
