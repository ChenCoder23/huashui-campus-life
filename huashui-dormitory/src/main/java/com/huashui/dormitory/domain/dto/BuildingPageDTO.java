package com.huashui.dormitory.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.domain.query.PageQuery;
import com.huashui.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



/**
 * 楼栋分页查询DTO
 */
@Data
@Schema(description = "楼栋分页查询DTO")
public class BuildingPageDTO extends PageQuery {


    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "楼栋编码")
    private String buildingCode;

    @Schema(description = "是否禁用")
    private Status status;

    @Schema(description = "所在校区")
    private Long campusId;


}