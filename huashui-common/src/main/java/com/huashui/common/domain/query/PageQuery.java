package com.huashui.common.domain.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    public <T> Page<T> toPage() {
        return new Page<>(this.getPageNum(), this.getPageSize());
    }
}