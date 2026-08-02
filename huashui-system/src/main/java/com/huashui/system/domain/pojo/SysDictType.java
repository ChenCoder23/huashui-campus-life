package com.huashui.system.domain.pojo;

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
@TableName("sys_dict_type")
@Schema(description = "字典类型表")
public class SysDictType {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型编码（全局唯一）")
    private String dictType;

    @Schema(description = "字典描述")
    private String description;

    @Schema(description = "状态")
    private Status status;

    private Long createBy;
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}