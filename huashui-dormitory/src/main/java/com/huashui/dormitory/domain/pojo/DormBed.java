package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.enums.Status;
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
public class DormBed {

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
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}