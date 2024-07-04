package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TaxTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TaxTemplate getTaxTemplateSample1() {
        return new TaxTemplate().percent(1).year(1).id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static TaxTemplate getTaxTemplateSample2() {
        return new TaxTemplate().percent(2).year(2).id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static TaxTemplate getTaxTemplateRandomSampleGenerator() {
        return new TaxTemplate().percent(intCount.incrementAndGet()).year(intCount.incrementAndGet()).id(UUID.randomUUID());
    }
}
