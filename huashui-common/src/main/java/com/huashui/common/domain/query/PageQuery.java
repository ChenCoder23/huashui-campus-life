package com.huashui.common.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分页查询基类")
public class PageQuery implements Serializable {

    @Schema(description = "页码")
    private Long pageNum = 1L;

    @Schema(description = "每页数量")
    private Long pageSize = 10L;

}