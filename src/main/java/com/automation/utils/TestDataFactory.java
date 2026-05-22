package com.automation.utils;

import com.github.javafaker.Faker;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Locale;

public class TestDataFactory {

    private static final Faker faker = new Faker(new Locale("en-US"));

    private TestDataFactory() {}

    public static CheckoutData generateCheckoutData() {
        return CheckoutData.builder()
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .postalCode(faker.address().zipCode())
            .build();
    }

    public static UserData generateUserData() {
        return UserData.builder()
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .email(faker.internet().emailAddress())
            .phone(faker.phoneNumber().cellPhone())
            .build();
    }

    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateRandomName() {
        return faker.name().fullName();
    }

    public static String generateRandomZipCode() {
        return faker.address().zipCode();
    }

    @Data
    @Builder
    public static class CheckoutData {
        private String firstName;
        private String lastName;
        private String postalCode;
    }

    @Data
    @Builder
    public static class UserData {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
    }
}
