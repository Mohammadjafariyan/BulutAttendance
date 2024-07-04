package com.bulut.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HrLetterParameterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHrLetterParameterAllPropertiesEquals(HrLetterParameter expected, HrLetterParameter actual) {
        assertHrLetterParameterAutoGeneratedPropertiesEquals(expected, actual);
        assertHrLetterParameterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHrLetterParameterAllUpdatablePropertiesEquals(HrLetterParameter expected, HrLetterParameter actual) {
        assertHrLetterParameterUpdatableFieldsEquals(expected, actual);
        assertHrLetterParameterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHrLetterParameterAutoGeneratedPropertiesEquals(HrLetterParameter expected, HrLetterParameter actual) {
        assertThat(expected)
            .as("Verify HrLetterParameter auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHrLetterParameterUpdatableFieldsEquals(HrLetterParameter expected, HrLetterParameter actual) {
        assertThat(expected)
            .as("Verify HrLetterParameter relevant properties")
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
            .satisfies(e -> assertThat(e.getOther()).as("check other").isEqualTo(actual.getOther()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHrLetterParameterUpdatableRelationshipsEquals(HrLetterParameter expected, HrLetterParameter actual) {
        assertThat(expected)
            .as("Verify HrLetterParameter relationships")
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getInternalUser()).as("check internalUser").isEqualTo(actual.getInternalUser()))
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()));
    }
}
