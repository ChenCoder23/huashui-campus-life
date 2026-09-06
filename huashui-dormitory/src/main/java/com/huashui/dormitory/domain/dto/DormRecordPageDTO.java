package com.huashui.dormitory.domain.dto;

import com.huashui.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "住宿记录分页查询DTO")
public class DormRecordPageDTO extends PageQuery {

    @Schema(description = "学期")
    private String semester;

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "学生姓名")
    private String studentName;
}
