package com.bulut.attendance.domain;

import java.util.UUID;

public class RecordStatusTestSamples {

    public static RecordStatus getRecordStatusSample1() {
        return new RecordStatus().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static RecordStatus getRecordStatusSample2() {
        return new RecordStatus().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static RecordStatus getRecordStatusRandomSampleGenerator() {
        return new RecordStatus().id(UUID.randomUUID());
    }
}
