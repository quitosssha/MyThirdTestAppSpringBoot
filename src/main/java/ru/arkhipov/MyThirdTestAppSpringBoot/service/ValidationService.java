package ru.arkhipov.MyThirdTestAppSpringBoot.service;

import org.springframework.validation.BindingResult;
import ru.arkhipov.MyThirdTestAppSpringBoot.exception.ValidationFailedException;

public interface ValidationService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
}
