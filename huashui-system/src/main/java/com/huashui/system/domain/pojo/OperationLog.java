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
@TableName("operation_log")
@Schema(description = "操作日志表")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long operatorId;

    @Schema(description = "操作人姓名")
    private String operatorName;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "操作模块")
    private String operationModule;

    @Schema(description = "操作描述")
    private String operationDesc;

    private String targetType;
    private Long targetId;
    private String requestIp;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String oldData;
    private String newData;
    private Integer costTime;
    private Integer status;
    private String errorMsg;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}