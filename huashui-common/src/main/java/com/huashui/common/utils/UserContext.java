package com.huashui.common.utils;

import com.huashui.common.exception.BusinessException;

/**
 * 当前用户上下文
 *
 * <p>
 * 用于保存当前 HTTP 请求对应的用户身份信息。
 * </p>
 *
 * <p>
 * 用户身份由 Gateway 鉴权后，通过：
 * X-User-Id
 * X-User-Role
 * 两个请求头传递到业务服务。
 * </p>
 */
public final class UserContext {

    /**
     * 当前用户 ID
     */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * 当前用户角色
     */
    private static final ThreadLocal<String> USER_ROLE = new ThreadLocal<>();

    private UserContext() {
    }

    /**
     * 设置当前用户 ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 设置当前用户角色
     */
    public static void setRole(String role) {
        USER_ROLE.set(role);
    }

    /**
     * 获取当前用户 ID
     */
    public static Long getUserId() {

        Long userId = USER_ID.get();

        if (userId == null) {
            throw new BusinessException("用户身份信息不存在");
        }

        return userId;
    }

    /**
     * 获取当前用户角色
     */
    public static String getRole() {

        String role = USER_ROLE.get();

        if (role == null || role.isBlank()) {
            throw new BusinessException("用户角色信息不存在");
        }

        return role;
    }

    /**
     * 判断当前用户是否拥有指定角色
     */
    public static boolean hasRole(String role) {
        return role != null && role.equals(USER_ROLE.get());
    }

    /**
     * 清理当前线程中的用户上下文
     *
     * <p>
     * 必须使用 remove()，不要使用 set(null)。
     * </p>
     */
    public static void clean() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}