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
@TableName("sys_dict_data")
@Schema(description = "字典数据表")
public class SysDictData {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典类型编码")
    private String dictType;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "是否默认选项")
    private Boolean isDefault;

    @Schema(description = "备注")
    private String remark;

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