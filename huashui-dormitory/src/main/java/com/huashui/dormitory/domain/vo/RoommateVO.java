package com.huashui.dormitory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "室友信息 VO")
public class RoommateVO {

    @Schema(description = "床位号")
    private String bedNumber;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "学号")
    private String studentId;

    @Schema(description = "头像URL")
    private String avatar;
}