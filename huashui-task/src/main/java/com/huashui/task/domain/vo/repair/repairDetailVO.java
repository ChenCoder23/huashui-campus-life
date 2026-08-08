package com.huashui.task.domain.vo.repair;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class repairDetailVO {


    private Long id;


    /**
     * 工单编号
     */
    private String orderNo;


    /**
     * 学生姓名
     */
    private String studentName;


    /**
     * 校区
     */
    private Long campusId;


    /**
     * 楼栋
     */
    private Long buildingId;

    /**
     * 校区名称
     */
    private String campusName;


    /**
     * 楼栋名称
     */
    private String buildingName;


    /**
     * 房间
     */
    private Long roomId;


    /**
     * 报修类型编码
     */
    private String repairType;


    /**
     * 报修类型名称
     */
    private String repairTypeName;


    private String description;

    //故障图片
    private List<String> images;


    /**
     * 联系电话
     */
    private String contactPhone;


    /**
     * 预约时间
     */
    private String appointmentTime;


    /**
     * 状态
     */
    private String status;


    /**
     * 维修人员
     */
    private String repairerName;


    /**
     * 维修结果
     */
    private String repairResult;


    private List<String> repairImages;


    private Integer rating;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

}