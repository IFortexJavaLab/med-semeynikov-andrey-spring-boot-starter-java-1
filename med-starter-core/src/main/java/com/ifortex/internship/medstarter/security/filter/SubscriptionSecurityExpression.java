package com.ifortex.internship.medstarter.security.filter;

import com.ifortex.internship.medstarter.exception.custom.AuthorizationException;
import com.ifortex.internship.medstarter.security.model.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
public class SubscriptionSecurityExpression {

    public static final String ANONYMOUS_USER = "anonymousUser";

    public boolean hasActiveSubscription(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        Object principle = authentication.getPrincipal();
        if (ANONYMOUS_USER.equals(principle.toString())) {
            log.error("Attempt to get user details by anonymous or unauthenticated user.");
            throw new AuthorizationException("User is not authenticated. Please log in.");
        }
        UserDetailsImpl user = (UserDetailsImpl) principle;

        boolean hasActiveSubscription = user.isHasActiveSubscription();
        boolean isSubscriptionValid = false;
        if (hasActiveSubscription) {
            LocalDateTime subscriptionEndDate = user.getSubscriptionEndDate();
            if (subscriptionEndDate != null) {
                isSubscriptionValid = subscriptionEndDate.isAfter(LocalDateTime.now(Clock.systemUTC()));
            }
        }

        return hasActiveSubscription && isSubscriptionValid;
    }
}
