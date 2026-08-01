package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.domain.pojo.BaseEntity;
import com.huashui.dormitory.Enum.BedStatus;
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
@TableName("dorm_bed")
@Schema(description = "床位表")
public class DormBed extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "房间ID")
    private Long roomId;

    @Schema(description = "床位号")
    private String bedNumber;

    @Schema(description = "入住学生ID")
    private Long studentId;

    @Schema(description = "床位状态（0-空闲，1-已入住，2-预留）")
    private BedStatus status;


}