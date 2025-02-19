package com.bulut.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AccProccParameterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccProccParameterAllPropertiesEquals(AccProccParameter expected, AccProccParameter actual) {
        assertAccProccParameterAutoGeneratedPropertiesEquals(expected, actual);
        assertAccProccParameterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccProccParameterAllUpdatablePropertiesEquals(AccProccParameter expected, AccProccParameter actual) {
        assertAccProccParameterUpdatableFieldsEquals(expected, actual);
        assertAccProccParameterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccProccParameterAutoGeneratedPropertiesEquals(AccProccParameter expected, AccProccParameter actual) {
        assertThat(expected)
            .as("Verify AccProccParameter auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccProccParameterUpdatableFieldsEquals(AccProccParameter expected, AccProccParameter actual) {
        assertThat(expected)
            .as("Verify AccProccParameter relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getManualOrAuto()).as("check manualOrAuto").isEqualTo(actual.getManualOrAuto()))
            .satisfies(e -> assertThat(e.getFormula()).as("check formula").isEqualTo(actual.getFormula()))
            .satisfies(e -> assertThat(e.getUnit()).as("check unit").isEqualTo(actual.getUnit()))
            .satisfies(e -> assertThat(e.getIsDeducTax()).as("check isDeducTax").isEqualTo(actual.getIsDeducTax()))
            .satisfies(e -> assertThat(e.getIsDeducInsurance()).as("check isDeducInsurance").isEqualTo(actual.getIsDeducInsurance()))
            .satisfies(e -> assertThat(e.getLaborTime()).as("check laborTime").isEqualTo(actual.getLaborTime()))
            .satisfies(e -> assertThat(e.getHokm()).as("check hokm").isEqualTo(actual.getHokm()))
            .satisfies(e -> assertThat(e.getEarnings()).as("check earnings").isEqualTo(actual.getEarnings()))
            .satisfies(e -> assertThat(e.getDeduction()).as("check deduction").isEqualTo(actual.getDeduction()))
            .satisfies(e -> assertThat(e.getOther()).as("check other").isEqualTo(actual.getOther()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccProccParameterUpdatableRelationshipsEquals(AccProccParameter expected, AccProccParameter actual) {
        assertThat(expected)
            .as("Verify AccProccParameter relationships")
            .satisfies(e -> assertThat(e.getInternalUser()).as("check internalUser").isEqualTo(actual.getInternalUser()))
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()));
    }
}
