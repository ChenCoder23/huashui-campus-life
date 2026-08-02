package com.huashui.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "字典类型 DTO")
public class DictTypeDTO {

    @NotBlank(message = "字典名称不能为空")
    @Schema(description = "字典名称")
    private String dictName;

    @NotBlank(message = "字典类型编码不能为空")
    @Schema(description = "字典类型编码")
    private String dictType;

    @Schema(description = "字典描述")
    private String description;
}