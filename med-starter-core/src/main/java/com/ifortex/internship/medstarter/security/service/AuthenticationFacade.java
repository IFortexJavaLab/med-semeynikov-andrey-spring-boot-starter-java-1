package com.ifortex.internship.medstarter.security.service;

import com.ifortex.internship.medstarter.exception.custom.AuthorizationException;
import com.ifortex.internship.medstarter.security.dto.AdminDetailsDto;
import com.ifortex.internship.medstarter.security.model.UserDetailsImpl;
import com.ifortex.internship.medstarter.security.model.constant.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static com.ifortex.internship.medstarter.security.filter.SubscriptionSecurityExpression.ANONYMOUS_USER;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.ROLE_LENGTH;

@Slf4j
public class AuthenticationFacade {

    public String getUserEmailFromAuthentication() {
        UserDetailsImpl principle = validateAuthenticatedUser();
        return principle.getEmail();
    }

    public String getUserFirstNameFromAuthentication() {
        UserDetailsImpl principle = validateAuthenticatedUser();
        return principle.getFirstName();
    }

    public UUID getAccountIdFromAuthentication() {
        UserDetailsImpl principle = validateAuthenticatedUser();
        return principle.getAccountId();
    }

    public AdminDetailsDto getAdminDetailsFromAuthentication() {
        UserDetailsImpl principle = validateAuthenticatedUser();
        List<UserRole> roles = principle.
            getAuthorities().stream()
            .map(authority ->
                UserRole.valueOf(authority.getAuthority().substring(ROLE_LENGTH)))
            .toList();

        UserRole roleType = null;
        if (!roles.isEmpty()) {
            roleType = roles.getFirst();
        }
        return new AdminDetailsDto(principle.getAccountId(),
            principle.getEmail(), roleType, principle.getIsSuperAdmin());
    }

    private UserDetailsImpl validateAuthenticatedUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ANONYMOUS_USER.equals(principle.toString())) {
            log.error("Attempt to get user details by anonymous or unauthenticated user.");
            throw new AuthorizationException("User is not authenticated. Please log in.");
        }
        return (UserDetailsImpl) principle;
    }
}
