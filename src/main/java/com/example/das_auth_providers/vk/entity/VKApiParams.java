package com.example.das_auth_providers.vk.entity;

import org.apache.tomcat.util.buf.StringUtils;

public final class VKApiParams {
    public final static String AUTH_CODE = "code";

    public final static String CLIENT_ID = "client_id";
    public final static String CLIENT_SECRET = "client_secret";
    public final static String SCOPE = "scope";
    public final static String STATE = "state";
    public final static String RESPONSE_TYPE = "response_type";
    public final static String VERSION = "v";
    public final static String REDIRECT_URI = "redirect_uri";

    public static final String USER_IDS = "user_ids";
    public static final String ACCESS_TOKEN = "access_token";

    private VKApiParams() {}
}
