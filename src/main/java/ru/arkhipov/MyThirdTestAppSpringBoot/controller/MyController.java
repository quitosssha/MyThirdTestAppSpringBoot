package ru.arkhipov.MyThirdTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MyThirdTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MyThirdTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MyThirdTestAppSpringBoot.model.*;
import ru.arkhipov.MyThirdTestAppSpringBoot.service.ValidationService;
import ru.arkhipov.MyThirdTestAppSpringBoot.util.DateTimeUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;

    @Autowired
    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }


    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult){

        log.info("Request: {}", request.toString());
        var parsedTime = Instant.from(DateTimeUtil.getCustomFormat().parse(request.getSystemTime()));
        var now = Instant.now().atZone(ZoneOffset.UTC);
        var duration = Duration.between(parsedTime, now);
        log.info("ping: {} ms", duration.toMillis());

        var uid = request.getUid();

        var response = Response.builder()
                .uid(uid)
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(Instant.now()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        try {
            validationService.isValid(bindingResult);
            ensureSupportedUid(uid);
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response: {}", response.toString());

            log.error("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Response: {}", response.toString());

            log.error("Unsupported code error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e){
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response: {}", response.toString());

            log.error("Unknown error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void ensureSupportedUid(String uid) throws UnsupportedCodeException {
        if (uid.equals("123"))
            throw new UnsupportedCodeException("unsupported uid: 123");
    }
}
