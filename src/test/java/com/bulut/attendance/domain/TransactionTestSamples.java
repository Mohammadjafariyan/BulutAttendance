package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction().id(1L).desc("desc1").docNumber("docNumber1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction().id(2L).desc("desc2").docNumber("docNumber2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction().id(longCount.incrementAndGet()).desc(UUID.randomUUID().toString()).docNumber(UUID.randomUUID().toString());
    }
}
