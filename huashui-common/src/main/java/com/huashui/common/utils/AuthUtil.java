package com.huashui.common.utils;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;

/**
 * 当前用户安全工具类
 *
 * <p>
 * 用于获取当前用户身份、判断角色以及执行角色权限校验。
 * </p>
 */
public final class AuthUtil {

    private AuthUtil() {
    }




    /**
     * 获取当前用户角色
     */
    public static String getRole() {
        return UserContext.getRole();
    }

    /**
     * 判断当前用户是否拥有指定角色
     */
    public static boolean hasRole(String role) {
        return role != null && role.equals(getRole());
    }

    /**
     * 判断当前用户是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }

    /**
     * 判断当前用户是否为宿舍管理员
     */
    public static boolean isDormManager() {
        return hasRole("DORM_MANAGER");
    }

    /**
     * 判断当前用户是否为保洁人员
     */
    public static boolean isCleaner() {
        return hasRole("CLEANER");
    }

    /**
     * 判断当前用户是否为维修人员
     */
    public static boolean isRepairer() {
        return hasRole("REPAIRER");
    }

    /**
     * 判断当前用户是否为学生
     */
    public static boolean isStudent() {
        return hasRole("STUDENT");
    }


}