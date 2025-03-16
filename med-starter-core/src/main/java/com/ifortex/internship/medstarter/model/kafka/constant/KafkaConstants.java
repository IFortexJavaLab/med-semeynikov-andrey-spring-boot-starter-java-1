package com.ifortex.internship.medstarter.model.kafka.constant;

public final class KafkaConstants {

    public static final String USER_DELETION_EVENTS = "user-deletion-events";
    public static final String ACCOUNTING_DELETION_GROUP = "user-deletion-group";
    public static final String KYC_VERIFICATION_EVENTS = "kyc-verification-events";
    public static final String KYC_VERIFICATION_GROUP = "kyc-verification-group";

    private KafkaConstants() {
        throw new IllegalStateException("Utility class");
    }
}