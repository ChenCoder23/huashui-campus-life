package com.huashui.system.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.enums.Status;
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
@TableName("sys_config")
@Schema(description = "系统配置表")
public class SysConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值")
    private String configValue;

    @Schema(description = "配置分组")
    private String configGroup;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置说明")
    private String description;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "状态")
    private Status status;

    private Long createBy;
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}