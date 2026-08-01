package com.huashui.dormitory.domain.dto;

import com.huashui.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 房间分页查询DTO
 */
@Data
@Schema(description = "房间分页查询DTO")
public class RoomPageDTO extends PageQuery {

    @Schema(description = "楼栋ID")
    private Long buildingId;

    @Schema(description = "楼层号")
    private Integer floorNumber;

    @Schema(description = "房间状态")
    private String status;
}