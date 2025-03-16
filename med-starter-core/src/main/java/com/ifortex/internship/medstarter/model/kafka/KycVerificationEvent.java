package com.ifortex.internship.medstarter.model.kafka;

import com.ifortex.internship.medstarter.model.kafka.constant.KycEventType;

import java.time.Instant;
import java.util.UUID;

public record KycVerificationEvent(
    KycEventType eventType,
    UUID accountId,
    Instant timestamp,
    String rejectionReason
) {
}