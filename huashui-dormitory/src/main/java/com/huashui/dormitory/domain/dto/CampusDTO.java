package com.huashui.dormitory.domain.dto;

import com.huashui.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "校区创建/编辑 DTO")
public class CampusDTO {

    @NotBlank(message = "校区名称不能为空")
    @Schema(description = "校区名称")
    private String campusName;

    @NotBlank(message = "校区编码不能为空")
    @Schema(description = "校区编码")
    private String campusCode;

    @Schema(description = "校区地址")
    private String address;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "状态（ENABLED/DISABLED）")
    private Status status;
}