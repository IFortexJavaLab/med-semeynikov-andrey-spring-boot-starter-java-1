package com.ifortex.internship.medstarter.security.filter;

import com.ifortex.internship.medstarter.exception.MedServiceException;
import com.ifortex.internship.medstarter.exception.custom.AuthorizationException;
import com.ifortex.internship.medstarter.security.model.UserDetailsImpl;
import com.ifortex.internship.medstarter.security.service.JwtTokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        try {

            log.debug("AuthTokenFilter started for: {}", request.getRequestURI());

            String jwt = parseJwt(request);

            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtTokenValidator.isValid(jwt)) {
                authenticateUser(jwt, request);
                filterChain.doFilter(request, response);
                return;
            }

            throw new AuthorizationException("Invalid JWT token");

        } catch (MedServiceException e) {
            log.error("Authentication service exception message: {}", e.getMessage());
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {

        log.debug("Authentication user started");

        String username = jwtTokenValidator.getUsernameFromToken(jwt);
        UUID userId = UUID.fromString(jwtTokenValidator.getUserIdFromToken(jwt));
        Boolean hasActiveSubscription = jwtTokenValidator.hasActiveSubscriptionFromToken(jwt);
        Boolean isSuperAdmin = jwtTokenValidator.isSuperAdmin(jwt);
        Optional<LocalDateTime> subscriptionEndDate = jwtTokenValidator.getSubscriptionEndDateFromToken(jwt);
        Collection<? extends GrantedAuthority> authorities = jwtTokenValidator.getAuthorityFromToken(jwt);

        UserDetailsImpl userDetails =
            UserDetailsImpl.builder()
                .email(username)
                .accountId(userId)
                .hasActiveSubscription(hasActiveSubscription)
                .authorities(authorities)
                .isSuperAdmin(isSuperAdmin)
                .build();

        subscriptionEndDate.ifPresent(userDetails::setSubscriptionEndDate);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("Set authentication for user: {}", userDetails.getEmail());
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader(AUTHORIZATION);
        return headerAuth != null && headerAuth.startsWith(BEARER_)
            ? headerAuth.substring(BEARER_PREFIX_LENGTH)
            : null;
    }
}
