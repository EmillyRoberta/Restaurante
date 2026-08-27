package br.com.fiap.restaurante.restaurante.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum UserType {
    CUSTOMER,
    RESTAURANT_OWNER;

    /**
     * Validate a Json String when converting to {@link UserType} and, in case of an invalid Enum,
     * shrinks the error message.
     *
     * @param userType Json String value
     * @return
     */
    @JsonCreator
    public static UserType fromString(String userType) {
        return Stream.of(UserType.values())
                     .filter(u -> u.name().equals(userType))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Unknown UserType: " + userType));
    }
}
