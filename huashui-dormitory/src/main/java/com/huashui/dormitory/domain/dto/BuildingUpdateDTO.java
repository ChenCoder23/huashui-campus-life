package com.huashui.dormitory.domain.dto;

import com.huashui.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "楼栋编辑 DTO")
public class BuildingUpdateDTO extends BuildingCreateDTO {

    @Schema(description = "状态（ENABLED/DISABLED）")
    private Status status;
}