package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HrLetterTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HrLetter getHrLetterSample1() {
        return new HrLetter()
            .title("title1")
            .uniqueNumber("uniqueNumber1")
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .bpmsApproveStatus(1);
    }

    public static HrLetter getHrLetterSample2() {
        return new HrLetter()
            .title("title2")
            .uniqueNumber("uniqueNumber2")
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .bpmsApproveStatus(2);
    }

    public static HrLetter getHrLetterRandomSampleGenerator() {
        return new HrLetter()
            .title(UUID.randomUUID().toString())
            .uniqueNumber(UUID.randomUUID().toString())
            .id(UUID.randomUUID())
            .bpmsApproveStatus(intCount.incrementAndGet());
    }
}
