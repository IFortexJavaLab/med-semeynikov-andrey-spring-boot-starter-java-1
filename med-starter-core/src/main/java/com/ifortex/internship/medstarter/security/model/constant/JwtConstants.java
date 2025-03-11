package com.ifortex.internship.medstarter.security.model.constant;

public final class JwtConstants {

    private JwtConstants() {
    }

    public static final String CLAIM_ACCOUNT_ID = "accountId";
    public static final String HAS_ACTIVE_SUBSCRIPTION_CLAIM = "hasActiveSubscription";
    public static final String IS_SUPER_ADMIN_CLAIM = "isSuperAdmin";
    public static final String CLAIM_SUBSCRIPTION_END_DATE = "subscriptionEndDate";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String CLAIM_ROLE = "role";
    public static final int ROLE_LENGTH = 5;
}