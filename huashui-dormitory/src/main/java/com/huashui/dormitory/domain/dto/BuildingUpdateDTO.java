package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "楼栋编辑 DTO")
public class BuildingUpdateDTO extends BuildingCreateDTO {
    // 继承 BuildingCreateDTO 所有字段，id 通过 URL 路径传递
}