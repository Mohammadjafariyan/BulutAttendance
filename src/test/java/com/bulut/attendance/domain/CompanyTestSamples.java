package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Company getCompanySample1() {
        return new Company().id(1L).title("title1").logo("logo1");
    }

    public static Company getCompanySample2() {
        return new Company().id(2L).title("title2").logo("logo2");
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).logo(UUID.randomUUID().toString());
    }
}
