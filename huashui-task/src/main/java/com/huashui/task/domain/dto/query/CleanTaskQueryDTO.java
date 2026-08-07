package com.huashui.task.domain.dto.query;

import com.huashui.common.domain.query.PageQuery;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CleanTaskQueryDTO extends PageQuery {


    /**
     * 执行人员
     */
    private Long workerId;


    /**
     * 校区
     */
    private Long campusId;


    /**
     * 楼栋
     */
    private Long buildingId;


    /**
     * 状态
     */
    private String status;


    /**
     * 开始日期
     */
    private LocalDate startDate;


    /**
     * 结束日期
     */
    private LocalDate endDate;

}