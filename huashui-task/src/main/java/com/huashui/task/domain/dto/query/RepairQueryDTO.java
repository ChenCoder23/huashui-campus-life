package com.huashui.task.domain.dto.query;

import com.huashui.common.domain.query.PageQuery;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RepairQueryDTO  extends PageQuery {


    /**
     * 报修状态
     */
    private String status;


    /**
     * 报修类型
     */
    private String repairType;


    /**
     * 开始时间
     */
    private LocalDate startDate;


    /**
     * 结束时间
     */
    private LocalDate endDate;

}