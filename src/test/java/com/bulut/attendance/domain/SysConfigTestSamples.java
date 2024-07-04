package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SysConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SysConfig getSysConfigSample1() {
        return new SysConfig()
            .taxFormula("taxFormula1")
            .sanavatFormula("sanavatFormula1")
            .year(1)
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static SysConfig getSysConfigSample2() {
        return new SysConfig()
            .taxFormula("taxFormula2")
            .sanavatFormula("sanavatFormula2")
            .year(2)
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static SysConfig getSysConfigRandomSampleGenerator() {
        return new SysConfig()
            .taxFormula(UUID.randomUUID().toString())
            .sanavatFormula(UUID.randomUUID().toString())
            .year(intCount.incrementAndGet())
            .id(UUID.randomUUID());
    }
}
