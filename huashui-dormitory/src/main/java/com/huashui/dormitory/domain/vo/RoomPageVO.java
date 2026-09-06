package com.huashui.dormitory.domain.vo;

import com.huashui.dormitory.Enum.RoomStatus;
import com.huashui.dormitory.Enum.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "房间分页返回VO")
public class RoomPageVO {

    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "房间号")
    private String roomNumber;

    @Schema(description = "所在楼层")
    private Integer floorNumber;

    @Schema(description = "房型（FOUR-四人间，SIX-六人间）")
    private RoomType roomType;

    @Schema(description = "总床位数")
    private Integer totalBeds;

    @Schema(description = "已入住人数")
    private Integer occupiedBeds;

    @Schema(description = "房间状态（NORMAL-正常，FULL-住满，EMPTY-空房，LOCKED-封闭）")
    private RoomStatus status;

    @Schema(description = "备注")
    private String remark;
}
