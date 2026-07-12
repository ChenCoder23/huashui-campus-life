package com.huashui.dormitory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 楼栋详情VO
 */
@Data
@Schema(description = "楼栋详情VO")
public class BuildingDetailVO {


    // ==================== 基础信息 ====================

    @Schema(description = "楼栋ID")
    private Long id;


    @Schema(description = "校区ID")
    private Long campusId;


    @Schema(description = "校区名称")
    private String campusName;


    @Schema(description = "片区")
    private String area;


    @Schema(description = "楼栋名称")
    private String buildingName;


    @Schema(description = "楼栋编码")
    private String buildingCode;


    @Schema(description = "房型")
    private String roomType;


    @Schema(description = "楼栋简介")
    private String description;


    @Schema(description = "是否标准化公寓")
    private Boolean standardized;



    // ==================== 楼栋规模 ====================

    @Schema(description = "总楼层")
    private Integer totalFloors;


    @Schema(description = "住宿费（元/年）")
    private Integer accommodationFee;

    @Schema(description = "是否有未住满的房间")
    private Boolean hasVacancy;

    @Schema(description = "未住满房间的数量")
    private Integer VacancyNum;




    // ==================== 楼栋配置 ====================

    @Schema(description = "是否独立卫浴")
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


    @Schema(description = "热水供应时间")
    private String hotWaterHours;


    @Schema(description = "是否有空调")
    private Boolean hasAc;


    @Schema(description = "是否有暖气")
    private Boolean hasHeating;


    @Schema(description = "是否有直饮水")
    private Boolean hasDrinkingWater;


    @Schema(description = "是否有洗衣机")
    private Boolean hasLaundry;


    @Schema(description = "是否有公共自习室")
    private Boolean hasStudyRoom;


    @Schema(description = "床铺尺寸")
    private String bedSize;



    // ==================== 统计信息 ====================

    @Schema(description = "房间总数")
    private Integer totalRooms;


    @Schema(description = "已入住人数")
    private Integer occupiedBeds;


    @Schema(description = "剩余床位数")
    private Integer availableBeds;


    @Schema(description = "入住率")
    private Double occupancyRate;



    // ==================== 管理信息 ====================

    @Schema(description = "管理员名称")
    private String managerName;


    @Schema(description = "管理员用户ID")
    private Long managerId;



    // ==================== 时间 ====================

    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}