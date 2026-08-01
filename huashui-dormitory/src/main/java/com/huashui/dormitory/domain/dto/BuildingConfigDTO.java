package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "楼栋硬件配置 DTO")
public class BuildingConfigDTO {

    @Schema(description = "是否有独立卫浴")
    private Boolean hasPrivateBath;

    @Schema(description = "卫浴类型")
    private String bathType;

    @Schema(description = "是否有阳台")
    private Boolean hasBalcony;

    @Schema(description = "阳台类型")
    private String balconyType;

    @Schema(description = "床铺类型")
    private String bedType;

    @Schema(description = "地面材质")
    private String floorType;

    @Schema(description = "热水供应类型")
    private String hotWaterType;

    @Schema(description = "热水供应时段")
    private String hotWaterHours;

    @Schema(description = "是否有空调位")
    private Boolean hasAc;

    @Schema(description = "是否有暖气")
    private Boolean hasHeating;

    @Schema(description = "是否有直饮水机")
    private Boolean hasDrinkingWater;

    @Schema(description = "是否有扫码洗衣机")
    private Boolean hasLaundry;

    @Schema(description = "是否有公共自习室")
    private Boolean hasStudyRoom;

    @Schema(description = "床铺尺寸")
    private String bedSize;
}