package com.huashui.dormitory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "楼栋分页返回VO")
public class BuildingPageVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "所属校区名称")
    private String campusName;

    @Schema(description = "片区分组")
    private String area;

    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "楼栋编码")
    private String buildingCode;

    @Schema(description = "房型")
    private String roomType;

    @Schema(description = "总层数")
    private Integer totalFloors;

    @Schema(description = "住宿费（元/年）")
    private Integer accommodationFee;

    @Schema(description = "是否标准化公寓")
    private Boolean isStandardized;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "是否有未住满的房间")
    private Boolean hasVacancy;

    @Schema(description = "未住满房间的数量")
    private Integer VacancyNum;

    @Schema(description = "管理员名称")
    private String managerName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;


}