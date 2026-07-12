package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.dormitory.Enum.RoomStatus;
import com.huashui.dormitory.Enum.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dorm_room")
@Schema(description = "房间表")
public class DormRoom {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "所属楼栋ID")
    private Long buildingId;

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

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除（0-未删除，1-已删除）")
    private Integer isDeleted;
}