package com.ifortex.internship.medstarter.model;

import com.ifortex.internship.medstarter.model.constant.KycEventType;

import java.time.Instant;
import java.util.UUID;

public record KycVerificationEvent(
    KycEventType eventType,
    UUID accountId,
    Instant timestamp,
    String rejectionReason
) {
}