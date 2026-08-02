package com.huashui.common.utils;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ROLES = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setRoles(String roles) {
        USER_ROLES.set(roles);
    }

    public static String getRoles() {
        return USER_ROLES.get();
    }

    public static void remove() {
        USER_ID.remove();
        USER_ROLES.remove();
    }
}
