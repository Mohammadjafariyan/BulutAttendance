package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Work getWorkSample1() {
        return new Work().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).desc("desc1").year(1).month(1);
    }

    public static Work getWorkSample2() {
        return new Work().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).desc("desc2").year(2).month(2);
    }

    public static Work getWorkRandomSampleGenerator() {
        return new Work()
            .id(UUID.randomUUID())
            .desc(UUID.randomUUID().toString())
            .year(intCount.incrementAndGet())
            .month(intCount.incrementAndGet());
    }
}
