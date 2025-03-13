package com.ifortex.internship.medstarter.security;

import com.ifortex.internship.medstarter.security.filter.AuthEntryPointJwt;
import com.ifortex.internship.medstarter.security.filter.CustomAccessDeniedHandler;
import com.ifortex.internship.medstarter.security.filter.SubscriptionSecurityExpression;
import com.ifortex.internship.medstarter.security.service.AuthenticationFacade;
import com.ifortex.internship.medstarter.security.service.JwtTokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SecurityAutoConfiguration {

    @Bean
    public JwtTokenValidator jwtTokenValidator(
        @Value("${app.jwtSecret}") String jwtSecret) {
        return new JwtTokenValidator(jwtSecret);
    }

    @Bean
    public AuthEntryPointJwt authEntryPointJwt() {
        return new AuthEntryPointJwt();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean(name = "subscriptionSecurity")
    public SubscriptionSecurityExpression subscriptionSecurityExpression() {
        return new SubscriptionSecurityExpression();
    }

    @Bean
    public AuthenticationFacade authenticationFacade() {
        return new AuthenticationFacade();
    }
}