package com.ifortex.internship.medstarter.security.service;

import com.ifortex.internship.medstarter.exception.custom.AuthorizationException;
import com.ifortex.internship.medstarter.security.model.constant.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;

import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.CLAIM_ACCOUNT_ID;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.CLAIM_FIRST_NAME;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.CLAIM_ROLE;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.CLAIM_SUBSCRIPTION_END_DATE;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.HAS_ACTIVE_SUBSCRIPTION_CLAIM;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.IS_SUPER_ADMIN_CLAIM;
import static com.ifortex.internship.medstarter.security.model.constant.JwtConstants.ROLE_PREFIX;

@Slf4j
@AllArgsConstructor
public class JwtTokenValidator {

    private final String jwtSecret;

    public boolean isValid(String authToken) {

        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            log.debug("Access token is valid");
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new AuthorizationException("JWT token is invalid. Please log in again.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new AuthorizationException("JWT token is malformed. Please log in again.");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new AuthorizationException("JWT token is unsupported. Please log in again.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new AuthorizationException("JWT claims string is empty. Please log in again.");
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        }

        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public String getFirstNameFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(CLAIM_FIRST_NAME, String.class);
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(CLAIM_ACCOUNT_ID, String.class);
    }

    public Boolean hasActiveSubscriptionFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(HAS_ACTIVE_SUBSCRIPTION_CLAIM, Boolean.class);
    }

    public Boolean isSuperAdmin(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(IS_SUPER_ADMIN_CLAIM, Boolean.class);
    }

    public Optional<LocalDateTime> getSubscriptionEndDateFromToken(String token) {
        Long subscriptionEndDate =
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(CLAIM_SUBSCRIPTION_END_DATE, Long.class);
        return Optional.ofNullable(subscriptionEndDate)
            .map(date -> LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC));
    }

    public Collection<SimpleGrantedAuthority> getAuthorityFromToken(String token) {
        log.debug("Getting authorities from access token");

        final Claims claims =
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        UserRole role = UserRole.valueOf(claims.get(CLAIM_ROLE, String.class));

        log.debug("Got roles from token: {}", role);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(ROLE_PREFIX + role.name()));
        log.debug("Made authority from roles: {}", authorities);

        return authorities;
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
