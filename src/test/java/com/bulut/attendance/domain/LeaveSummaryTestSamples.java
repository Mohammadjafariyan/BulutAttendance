package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaveSummaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LeaveSummary getLeaveSummarySample1() {
        return new LeaveSummary().remainHours(1).remainDays(1).year(1).id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static LeaveSummary getLeaveSummarySample2() {
        return new LeaveSummary().remainHours(2).remainDays(2).year(2).id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static LeaveSummary getLeaveSummaryRandomSampleGenerator() {
        return new LeaveSummary()
            .remainHours(intCount.incrementAndGet())
            .remainDays(intCount.incrementAndGet())
            .year(intCount.incrementAndGet())
            .id(UUID.randomUUID());
    }
}
