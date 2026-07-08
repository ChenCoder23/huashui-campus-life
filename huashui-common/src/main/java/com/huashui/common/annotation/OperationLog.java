package com.huashui.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * <p>
 * 标记在 Controller 方法上，由 huashui-system 的 AOP 切面自动记录。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /** 操作模块，如 "宿舍管理" */
    String module() default "";

    /** 操作类型，如 "新增房间" */
    String action() default "";
}
