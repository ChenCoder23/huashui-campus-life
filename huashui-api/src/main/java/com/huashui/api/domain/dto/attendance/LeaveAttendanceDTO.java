package com.huashui.api.domain.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 请假联动考勤 DTO（跨服务：leave -> attendance）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveAttendanceDTO {

    /** 工作人员ID（保洁员 = 请假申请人ID） */
    private Long workerId;

    /** 工作人员姓名快照 */
    private String workerName;

    /** 校区ID */
    private Long campusId;

    /** 请假开始日期 */
    private LocalDate startDate;

    /** 请假结束日期 */
    private LocalDate endDate;

    /** 备注 */
    private String remark;
}
