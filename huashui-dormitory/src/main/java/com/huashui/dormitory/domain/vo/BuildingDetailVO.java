package com.huashui.dormitory.domain.vo;

import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.pojo.DormBuildingConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "楼栋详情 VO（含配置）")
public class BuildingDetailVO extends DormBuilding {

    @Schema(description = "楼栋硬件配置")
    private DormBuildingConfig config;
}