package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AccountTemplate getAccountTemplateSample1() {
        return new AccountTemplate()
            .title("title1")
            .code("code1")
            .level(1)
            .levelTitle("levelTitle1")
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AccountTemplate getAccountTemplateSample2() {
        return new AccountTemplate()
            .title("title2")
            .code("code2")
            .level(2)
            .levelTitle("levelTitle2")
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AccountTemplate getAccountTemplateRandomSampleGenerator() {
        return new AccountTemplate()
            .title(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .level(intCount.incrementAndGet())
            .levelTitle(UUID.randomUUID().toString())
            .id(UUID.randomUUID());
    }
}
