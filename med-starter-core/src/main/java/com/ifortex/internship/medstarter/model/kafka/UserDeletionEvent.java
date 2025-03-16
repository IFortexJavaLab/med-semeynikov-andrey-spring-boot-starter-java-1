package com.ifortex.internship.medstarter.model.kafka;

import java.time.Instant;
import java.util.UUID;

public record UserDeletionEvent(
    UUID accountId,
    Instant deletedAt
) {
}