package com.huashui.dormitory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "学生宿舍首页 VO")
public class DormHomeVO {

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "片区")
    private String area;

    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "房间号")
    private String roomNumber;

    @Schema(description = "床位号")
    private String bedNumber;

    @Schema(description = "楼层")
    private Integer floorNumber;

    @Schema(description = "房型")
    private String roomType;

    @Schema(description = "宿舍总人数")
    private Integer totalBeds;

    @Schema(description = "已入住人数")
    private Integer occupiedBeds;

    @Schema(description = "住宿费（元/年）")
    private Integer accommodationFee;

    @Schema(description = "是否有独立卫浴")
    private Boolean hasPrivateBath;

    @Schema(description = "是否有阳台")
    private Boolean hasBalcony;

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
}