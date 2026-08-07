package com.huashui.common.utils;

import java.time.LocalDate;

/**
 * 学期工具类
 */
public class SemesterUtil {


    private SemesterUtil() {
    }


    /**
     * 获取当前学期
     *
     * 格式:
     * 2026_1
     * 2026_2
     *
     * 规则:
     * 2-7月 第一学期
     * 8-次年1月 第二学期
     *
     * @return 当前学期
     */
    public static String getCurrentSemester() {

        LocalDate now = LocalDate.now();

        int year = now.getYear();

        int month = now.getMonthValue();


        if (month >= 2 && month <= 7) {

            return year + "_1";

        } else {

            // 8-12月属于当前年份第二学期
            // 1月属于上一年份第二学期
            if (month == 1) {
                return (year - 1) + "_2";
            }

            return year + "_2";
        }
    }
}