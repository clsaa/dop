package com.clsaa.dop.server.gateway.oauth.protocol;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

/**
 * OAuth授权类型
 *
 * @author 任贵杰
 * @since 2018-12-29
 */
public final class GrantType {
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final ImmutableSet<String> grantTypeSet = ImmutableSet.of(CLIENT_CREDENTIALS);

    private GrantType() {
        throw new UnsupportedOperationException();
    }

    /**
     * 是否支持此类授权
     *
     * @param type
     */
    public static boolean enabled(String type) {
        return !Strings.isNullOrEmpty(type) && grantTypeSet.contains(type);
    }

    public static boolean isClientCredentials(String type) {
        return CLIENT_CREDENTIALS.equals(type);
    }

}
