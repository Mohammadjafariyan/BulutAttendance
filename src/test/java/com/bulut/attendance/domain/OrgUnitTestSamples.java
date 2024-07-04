package com.bulut.attendance.domain;

import java.util.UUID;

public class OrgUnitTestSamples {

    public static OrgUnit getOrgUnitSample1() {
        return new OrgUnit().title("title1").id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static OrgUnit getOrgUnitSample2() {
        return new OrgUnit().title("title2").id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static OrgUnit getOrgUnitRandomSampleGenerator() {
        return new OrgUnit().title(UUID.randomUUID().toString()).id(UUID.randomUUID());
    }
}
