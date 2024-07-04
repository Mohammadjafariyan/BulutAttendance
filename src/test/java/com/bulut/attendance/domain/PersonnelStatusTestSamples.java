package com.bulut.attendance.domain;

import java.util.UUID;

public class PersonnelStatusTestSamples {

    public static PersonnelStatus getPersonnelStatusSample1() {
        return new PersonnelStatus().title("title1").id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static PersonnelStatus getPersonnelStatusSample2() {
        return new PersonnelStatus().title("title2").id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static PersonnelStatus getPersonnelStatusRandomSampleGenerator() {
        return new PersonnelStatus().title(UUID.randomUUID().toString()).id(UUID.randomUUID());
    }
}
