package com.bulut.attendance.domain;

import java.util.UUID;

public class BankTestSamples {

    public static Bank getBankSample1() {
        return new Bank().title("title1").code("code1").id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Bank getBankSample2() {
        return new Bank().title("title2").code("code2").id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Bank getBankRandomSampleGenerator() {
        return new Bank().title(UUID.randomUUID().toString()).code(UUID.randomUUID().toString()).id(UUID.randomUUID());
    }
}
