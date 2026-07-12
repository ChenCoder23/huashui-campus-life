package com.huashui.dormitory.domain.dto;

import com.huashui.dormitory.Enum.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 新增楼栋DTO
 */
@Data
@Schema(description = "新增楼栋DTO")
public class BuildingDTO {

    @Schema(description = "主键ID，新增时为空，修改时必填")
    private Long id;


    @Schema(description = "所属校区ID")
    @NotNull(message = "校区不能为空")
    private Long campusId;


    @Schema(description = "片区分组")
    @NotBlank(message = "片区不能为空")
    @Size(max = 16, message = "片区长度不能超过16")
    private String area;


    @Schema(description = "楼栋名称")
    @NotBlank(message = "楼栋名称不能为空")
    @Size(max = 32, message = "楼栋名称长度不能超过32")
    private String buildingName;


    @Schema(description = "楼栋编码")
    @NotBlank(message = "楼栋编码不能为空")
    @Size(max = 16, message = "楼栋编码长度不能超过16")
    private String buildingCode;


    @Schema(description = "房型【FOUR-四人间,SIX-六人间】")
    @NotBlank(message = "房型不能为空")
    private RoomType roomType;


    @Schema(description = "总层数")
    @NotNull(message = "楼层数不能为空")
    @Min(value = 1, message = "楼层数至少为1")
    private Integer totalFloors;


    @Schema(description = "住宿费（元/年）")
    @NotNull(message = "住宿费不能为空")
    @Min(value = 0, message = "住宿费不能小于0")
    private Integer accommodationFee;


    @Schema(description = "楼栋简介（不填写时自动生成）")
    @Size(max = 256, message = "简介长度不能超过256")
    private String description;


    @Schema(description = "是否标准化公寓")
    private Boolean standardized;


    @Schema(description = "排序序号")
    private Integer sortOrder;

}