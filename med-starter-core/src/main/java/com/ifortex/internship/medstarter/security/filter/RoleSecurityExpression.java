package com.ifortex.internship.medstarter.security.filter;

import com.ifortex.internship.medstarter.exception.custom.AuthorizationException;
import com.ifortex.internship.medstarter.security.model.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class RoleSecurityExpression {

    public static final String ANONYMOUS_USER = "anonymousUser";

    public boolean isSuperAdmin(Authentication authentication) {
        if (authentication == null || ANONYMOUS_USER.equals(authentication.getPrincipal().toString())) {
            log.error("Attempt to access Super Admin functionality by unauthenticated user.");
            throw new AuthorizationException("User is not authenticated. Please log in.");
        }

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        if (Boolean.TRUE.equals(user.getIsSuperAdmin())) {
            return true;
        }

        log.warn("User {} attempted to access Super Admin functionality without permission.", user.getEmail());
        return false;
    }
}
