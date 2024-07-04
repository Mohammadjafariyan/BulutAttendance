package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccProcStepExecutionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccProcStepExecution getAccProcStepExecutionSample1() {
        return new AccProcStepExecution().id(1L).desc("desc1");
    }

    public static AccProcStepExecution getAccProcStepExecutionSample2() {
        return new AccProcStepExecution().id(2L).desc("desc2");
    }

    public static AccProcStepExecution getAccProcStepExecutionRandomSampleGenerator() {
        return new AccProcStepExecution().id(longCount.incrementAndGet()).desc(UUID.randomUUID().toString());
    }
}
