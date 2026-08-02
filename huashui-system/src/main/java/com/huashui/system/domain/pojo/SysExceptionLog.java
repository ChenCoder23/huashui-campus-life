package com.huashui.system.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_exception_log")
@Schema(description = "异常日志表")
public class SysExceptionLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String exceptionName;
    private String exceptionMsg;
    private String stackTrace;
    private String serviceName;
    private String requestUrl;
    private String requestMethod;
    private String requestParams;
    private Long operatorId;
    private String ipAddress;
    private Integer status;
    private Long handlerId;
    private LocalDateTime handleTime;
    private String handleRemark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}