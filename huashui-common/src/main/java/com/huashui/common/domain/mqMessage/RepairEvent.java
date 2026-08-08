package com.huashui.common.domain.mqMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RepairEvent {

    /**
     * 工单ID
     */
    private Long repairId;


    /**
     * 工单编号
     */
    private String orderNo;


    /**
     * 学生ID
     */
    private Long studentId;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 房间ID
     */
    private Long roomId;


    /**
     * 维修工ID
     */
    private Long repairerId;


    /**
     * 报修类型
     */
    private String repairType;


    /**
     * 问题描述
     */
    private String description;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;



    /**
     * 事件类型
     * CREATE-创建
     * CANCEL-取消
     * ASSIGN-派单
     * COMPLETE-完成
     */
    private String eventType;

    /**
     * 设置问题描述
     * 最大15字，超出截断
     */
    public static String formatDescription(String description){

        if(description == null){
            return null;
        }

        if(description.length()>15){

            return description.substring(0,15)
                    +"...";

        }
        return description;
    }



}