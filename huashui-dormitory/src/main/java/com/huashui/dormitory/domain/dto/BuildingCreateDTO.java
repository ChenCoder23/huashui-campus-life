package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "楼栋创建 DTO（含配置）")
public class BuildingCreateDTO {

    @NotNull(message = "校区ID不能为空")
    @Schema(description = "所属校区ID")
    private Long campusId;

    @Schema(description = "片区分组")
    private String area;

    @NotBlank(message = "楼栋名称不能为空")
    @Schema(description = "楼栋名称")
    private String buildingName;

    @NotBlank(message = "楼栋编码不能为空")
    @Schema(description = "楼栋编码")
    private String buildingCode;

    @NotBlank(message = "房型不能为空")
    @Schema(description = "房型（FOUR/SIX）")
    private String roomType;

    @NotNull(message = "总层数不能为空")
    @Schema(description = "总层数")
    private Integer totalFloors;

    @Schema(description = "住宿费（元/年）")
    private Integer accommodationFee;

    @Schema(description = "楼栋简介")
    private String description;

    @Schema(description = "是否标准化公寓")
    private Boolean isStandardized;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    // 楼栋配置
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