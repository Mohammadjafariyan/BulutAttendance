package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonnelTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Personnel getPersonnelSample1() {
        return new Personnel()
            .firstName("firstName1")
            .lastName("lastName1")
            .requitmentDate("requitmentDate1")
            .father("father1")
            .shenasname("shenasname1")
            .mahalesodur("mahalesodur1")
            .birthday("birthday1")
            .isSingle("isSingle1")
            .lastEducation("lastEducation1")
            .educationField("educationField1")
            .children(1)
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Personnel getPersonnelSample2() {
        return new Personnel()
            .firstName("firstName2")
            .lastName("lastName2")
            .requitmentDate("requitmentDate2")
            .father("father2")
            .shenasname("shenasname2")
            .mahalesodur("mahalesodur2")
            .birthday("birthday2")
            .isSingle("isSingle2")
            .lastEducation("lastEducation2")
            .educationField("educationField2")
            .children(2)
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Personnel getPersonnelRandomSampleGenerator() {
        return new Personnel()
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .requitmentDate(UUID.randomUUID().toString())
            .father(UUID.randomUUID().toString())
            .shenasname(UUID.randomUUID().toString())
            .mahalesodur(UUID.randomUUID().toString())
            .birthday(UUID.randomUUID().toString())
            .isSingle(UUID.randomUUID().toString())
            .lastEducation(UUID.randomUUID().toString())
            .educationField(UUID.randomUUID().toString())
            .children(intCount.incrementAndGet())
            .id(UUID.randomUUID());
    }
}
