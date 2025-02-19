package com.bulut.attendance.domain;

import java.util.UUID;

public class WorkItemTestSamples {

    public static WorkItem getWorkItemSample1() {
        return new WorkItem().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static WorkItem getWorkItemSample2() {
        return new WorkItem().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static WorkItem getWorkItemRandomSampleGenerator() {
        return new WorkItem().id(UUID.randomUUID());
    }
}
