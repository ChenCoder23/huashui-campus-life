package com.huashui.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "系统配置 DTO")
public class ConfigDTO {

    @NotBlank(message = "配置键不能为空")
    @Schema(description = "配置键")
    private String configKey;

    @NotBlank(message = "配置值不能为空")
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
}