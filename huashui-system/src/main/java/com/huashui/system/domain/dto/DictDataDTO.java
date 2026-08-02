package com.huashui.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "字典数据 DTO")
public class DictDataDTO {

    @NotBlank(message = "字典类型编码不能为空")
    @Schema(description = "字典类型编码")
    private String dictType;

    @NotBlank(message = "字典标签不能为空")
    @Schema(description = "字典标签")
    private String dictLabel;

    @NotBlank(message = "字典键值不能为空")
    @Schema(description = "字典键值")
    private String dictValue;

    @NotNull(message = "排序序号不能为空")
    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "是否默认")
    private Boolean isDefault;

    @Schema(description = "备注")
    private String remark;
}