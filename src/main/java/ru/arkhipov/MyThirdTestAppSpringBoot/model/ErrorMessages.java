package ru.arkhipov.MyThirdTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessages {

    EMPTY(""),
    VALIDATION("Ошибка валидации"),
    UNSUPPORTED("Произошла непредвиденная ошибка"),
    UNKNOWN("Не поддерживаемая ошибка");

    private final String description;

    ErrorMessages(String name){
        this.description = name;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

}
