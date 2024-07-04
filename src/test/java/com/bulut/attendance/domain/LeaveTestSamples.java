package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaveTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Leave getLeaveSample1() {
        return new Leave().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).bpmsApproveStatus(1);
    }

    public static Leave getLeaveSample2() {
        return new Leave().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).bpmsApproveStatus(2);
    }

    public static Leave getLeaveRandomSampleGenerator() {
        return new Leave().id(UUID.randomUUID()).bpmsApproveStatus(intCount.incrementAndGet());
    }
}
