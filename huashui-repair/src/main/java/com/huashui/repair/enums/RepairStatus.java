package com.huashui.repair.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepairStatus {
    PENDING("PENDING", "待受理"),
    ASSIGNED("ASSIGNED", "已派单"),
    REPAIRING("REPAIRING", "维修中"),
    COMPLETED("COMPLETED", "已完成"),
    EVALUATED("EVALUATED", "已评价"),
    CANCELLED("CANCELLED", "已取消");
    private final String code;
    private final String desc;
}